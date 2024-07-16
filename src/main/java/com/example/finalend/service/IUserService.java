package com.example.finalend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.finalend.entity.User;
import com.example.finalend.param.UserParam;
import com.example.finalend.vo.LoginVo;

public interface IUserService extends IService<User> {
    LoginVo login(String account, String password);

    LoginVo register(UserParam userParam);
}
