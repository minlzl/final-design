package com.example.finalend.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.example.finalend.annotation.ApiRestController;
import com.example.finalend.param.ReplyParam;
import com.example.finalend.service.impl.ReplyService;
import com.example.finalend.vo.PageVo;
import com.example.finalend.vo.ReplyVo;
import com.example.finalend.vo.Resp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

@ApiRestController
public class ReplyController {

    @Resource
    ReplyService replyService;

    @GetMapping("/reply")
    public Resp<PageVo<ReplyVo>> getReplies(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                            Long id) {
        PageVo<ReplyVo> replies = replyService.getReplies(pageNum, pageSize, id);
        return Resp.success(replies);
    }

    @PostMapping("/reply")
    @SaCheckLogin
    public Resp<ReplyVo> pubReply(@RequestBody ReplyParam replyParam) {
        ReplyVo replyVo = replyService.saveReply(replyParam);
        return Resp.success(replyVo);
    }
}
