package com.example.finalend.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Assert;
import com.example.finalend.annotation.ApiRestController;
import com.example.finalend.param.UserParam;
import com.example.finalend.service.impl.LoginService;
import com.example.finalend.vo.LoginVo;
import com.example.finalend.vo.Resp;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

@ApiRestController
public class LoginController {

    @Resource
    LoginService loginService;


    @RequestMapping("/login")
    public Resp<LoginVo> login(String account,
                              String password) {
        Assert.notNull(account, "参数不可以为空");
        Assert.notNull(password, "参数不可以为空");
        LoginVo login = loginService.login(account, password);
        return Resp.success(login);
    }

    @RequestMapping("/logout")
    @SaCheckLogin
    public Resp<Object> logout() {
        StpUtil.logout();
        return Resp.success();
    }

    @PostMapping("/register")
    public Resp<LoginVo> register(@RequestBody UserParam userParam) {
        LoginVo loginVo = loginService.register(userParam);
        return Resp.success(loginVo);
    }
}
