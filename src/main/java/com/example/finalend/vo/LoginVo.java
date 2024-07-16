package com.example.finalend.vo;

import cn.dev33.satoken.stp.SaTokenInfo;
import com.example.finalend.entity.User;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class LoginVo implements Serializable {

    private String token;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String account;

    private Byte role;

    private String name;

    private String email;

    private String description;

    private LocalDateTime updateTime;

    private LocalDateTime loginTime;

    public static LoginVo of(SaTokenInfo tokenInfo, User user, LocalDateTime loginTime) {
        if (tokenInfo == null || user == null) {
            return null;
        }
        LoginVo loginVo = new LoginVo();
        BeanUtils.copyProperties(user, loginVo);
        loginVo.setToken(tokenInfo.tokenValue);
        loginVo.setLoginTime(loginTime);
        return loginVo;
    }

    public static LoginVo of(User u) {
        if (u == null) {
            return null;
        }
        LoginVo userVo = new LoginVo();
        BeanUtils.copyProperties(u, userVo);
        return userVo;
    }
}
