package com.example.finalend.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.example.finalend.annotation.ApiRestController;
import com.example.finalend.param.CommentParam;
import com.example.finalend.param.TalkParam;
import com.example.finalend.service.impl.CommentService;
import com.example.finalend.vo.CommentVo;
import com.example.finalend.vo.PageVo;
import com.example.finalend.vo.Resp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

@ApiRestController
public class CommentController {

    @Resource
    CommentService commentService;

    @GetMapping("/comment")
    public Resp<PageVo<CommentVo>> getComments(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                            Long id) {
        PageVo<CommentVo> comments = commentService.getComments(pageNum, pageSize, id);
        return Resp.success(comments);
    }

    @PostMapping("/comment")
    @SaCheckLogin
    public Resp<String> pubComment(@RequestBody CommentParam commentParam) {
        commentService.saveComment(commentParam);
        return Resp.success();
    }
}
