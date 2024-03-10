package com.example.finalend.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import com.example.finalend.annotation.ApiRestController;
import com.example.finalend.config.FinalDesignProperty;
import com.example.finalend.service.impl.ImageService;
import com.example.finalend.service.impl.UserService;
import com.example.finalend.vo.Resp;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@ApiRestController
@SaCheckLogin
public class ImageController {

    @Resource
    ImageService imageService;

    @Resource
    UserService userService;

    @PostMapping("/upload")
    public Resp<String> upload(@RequestParam("file")MultipartFile multipartFile) {
        String res = imageService.save(multipartFile);
        return Resp.success(res);
    }

    @PostMapping("/avatar")
    public Resp<String> avatar(@RequestParam("file")MultipartFile multipartFile) {
        String res = imageService.save(multipartFile);
        userService.updateAvatar(res);
        return Resp.success(res);
    }
}
