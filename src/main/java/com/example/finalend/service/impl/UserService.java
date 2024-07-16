package com.example.finalend.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.finalend.entity.User;
import com.example.finalend.exception.ApiException;
import com.example.finalend.mapper.TalkLoveMapper;
import com.example.finalend.mapper.UserMapper;
import com.example.finalend.relation.TalkLove;
import com.example.finalend.vo.UserVo;
import com.google.protobuf.Api;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {

    @Resource
    UserMapper userMapper;

    @Resource
    TalkLoveMapper talkLoveMapper;

    public String getName(Long id) {
        if (id == null) {
            throw new ApiException("参数错误");
        }
        User user = userMapper.selectOne(new QueryWrapper<User>().select("name").eq("id", id));
        if (user == null) {
            throw new ApiException("数据库中无此用户");
        }
        return user.getName();
    }

    public void updateAvatar(String res) {
        long loginId = StpUtil.getLoginIdAsLong();
        User user = new User();
        user.setAvatar(res);
        user.setId(loginId);
        userMapper.updateById(user);
    }

    public UserVo getUser(Long id) {
        UserVo userVo = new UserVo();
        User user = userMapper.selectById(id);
        BeanUtils.copyProperties(user, userVo);
        return userVo;
    }
}
