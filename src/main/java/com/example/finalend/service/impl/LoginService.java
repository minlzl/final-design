package com.example.finalend.service.impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.finalend.entity.User;
import com.example.finalend.exception.ApiException;
import com.example.finalend.mapper.UserMapper;
import com.example.finalend.param.UserParam;
import com.example.finalend.service.IUserService;
import com.example.finalend.vo.LoginVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Service
public class LoginService extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    UserMapper userMapper;

    private static final String ACCOUNT_PATTERN = "^[a-zA-Z0-9_-]{3,15}$";
    private static final String PASSWORD_PATTERN = "^[a-zA-Z0-9_#?!@$%^&*-]{3,15}$";
    private static final String EMAIL_PATTERN = "[^@ \\t\\r\\n]+@[^@ \\t\\r\\n]+\\.[^@ \\t\\r\\n]+";
    private static final String SALT = "SALT-FINAL-DESIGN";
    private static final String DEFAULT_AVATAR = "https://s2.loli.net/2023/11/15/3Lfpqw2enQlKsWM.png";


    @Override
    public LoginVo login(String account, String rawPassword) {
        if (!ReUtil.isMatch(ACCOUNT_PATTERN, account)) {
            throw new ApiException("账号的格式不符合要求");
        }
        if (!ReUtil.isMatch(PASSWORD_PATTERN, rawPassword)) {
            throw new ApiException("密码格式不符合要求");
        }
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("account", account));
        if (user == null) {
            throw new ApiException("用户不存在");
        }
        String password = SaSecureUtil.md5BySalt(rawPassword, SALT);
        if (!StrUtil.equals(password, user.getPassword())) {
            throw new ApiException("密码错误");
        }
        StpUtil.login(user.getId());
        return LoginVo.of(StpUtil.getTokenInfo(), user, LocalDateTime.now());
    }

    @Override
    @Transactional
    public LoginVo register(UserParam userParam) {
        Assert.notNull(userParam, "数据不可以为空");
        String account = userParam.getAccount();
        if (!ReUtil.isMatch(ACCOUNT_PATTERN, account)) {
            throw new ApiException("账号的格式不符合要求");
        }
        String password = userParam.getPassword();
        if (!ReUtil.isMatch(PASSWORD_PATTERN, password)) {
            throw new ApiException("密码格式不符合要求");
        }
        String email = userParam.getEmail();
        if (StrUtil.isBlank(email)) {
            email = "";
        } else {
            if (!ReUtil.isMatch(EMAIL_PATTERN, email)) {
                throw new ApiException("邮箱的格式不符合要求");
            }
        }
        String name = userParam.getName();
        if (StrUtil.isBlank(name)) {
            name = "匿名-" + com.example.finalend.utils.StrUtil.getRandomString(4);
        }
        if (userMapper.exists(new QueryWrapper<User>().eq("account", account))) {
            throw new ApiException("账号已被使用");
        }
        User user = new User();
        try {
            user.setId(null);
            user.setAccount(account);
            user.setPassword(SaSecureUtil.md5BySalt(password, SALT));
            user.setName(name);
            user.setDeleted(false);
            user.setRole(Byte.valueOf("0"));
            user.setDescription("");
            user.setCreateTime(LocalDateTime.now());
            user.setUpdateTime(LocalDateTime.now());
            user.setEmail(email);
            user.setAvatar(DEFAULT_AVATAR);
            save(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException("添加失败");
        }
        return LoginVo.of(user);
    }
}
