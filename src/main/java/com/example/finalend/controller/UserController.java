package com.example.finalend.controller;

import com.example.finalend.annotation.ApiRestController;
import com.example.finalend.exception.ApiException;
import com.example.finalend.service.impl.UserService;
import com.example.finalend.vo.Resp;
import com.example.finalend.vo.UserVo;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;

@ApiRestController
public class UserController {

    @Resource
    UserService userService;

    @GetMapping("/user/name")
    public Resp<String> getName(Long id) {
        return Resp.success(userService.getName(id));
    }

    @GetMapping("/userInfo")
    public Resp<UserVo> getUser(Long id) {
        if (id == null) {
            throw new ApiException("参数错误");
        }
        UserVo user = userService.getUser(id);
        return Resp.success(user);
    }

}
