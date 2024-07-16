package com.example.finalend.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.example.finalend.annotation.ApiRestController;
import com.example.finalend.entity.Article;
import com.example.finalend.entity.User;
import com.example.finalend.param.UserParam;
import com.example.finalend.service.impl.AdminService;
import com.example.finalend.vo.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@ApiRestController
@SaCheckLogin
public class AdminController {

    @Resource
    AdminService adminService;

    @GetMapping("/user")
    public Resp<User> getUser() {
        User user = adminService.getUser();
        user.setPassword(null);
        return Resp.success(user);
    }

    @PostMapping("/user/updateUser")
    public Resp<User> update(@RequestBody UserParam userParam) {
        System.out.println(userParam);
        User u = adminService.update(userParam);
        return Resp.success(u);
    }

    @GetMapping("/user/talks")
    public Resp<PageVo<TalkVo>> getAllTalk(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                           @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        PageVo<TalkVo> talks = adminService.getTalks(pageNum, pageSize);
        return Resp.success(talks);
    }

    @GetMapping("/user/articles")
    public Resp<PageVo<ArticleVo>> getAllArticle(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                 @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        PageVo<ArticleVo> articles = adminService.getArticles(pageNum, pageSize);
        return Resp.success(articles);
    }

    @GetMapping("/user/history")
    public Resp<PageVo<HistoryVo>> getHistory(@RequestParam(value = "pageNum", defaultValue = "1") Long pageNum,
                                              @RequestParam(value = "pageSize", defaultValue = "10") Long pageSize) {
        PageVo<HistoryVo> history = adminService.getHistory(pageNum, pageSize);
        return Resp.success(history);
    }

    @GetMapping("/user/myComments")
    public Resp<PageVo<MyCommentVo>> getMyComments(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                   @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        PageVo<MyCommentVo> myComments = adminService.getMyComments(pageNum, pageSize);
        return Resp.success(myComments);
    }

}
