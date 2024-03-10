package com.example.finalend.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import com.example.finalend.annotation.ApiRestController;
import com.example.finalend.entity.Talk;
import com.example.finalend.param.TalkParam;
import com.example.finalend.service.impl.TalkService;
import com.example.finalend.vo.Resp;
import com.example.finalend.vo.TalkVo;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@ApiRestController
@SaCheckLogin
public class TalkController {

    @Resource
    TalkService talkService;

    @GetMapping("/talk")
    public Resp<List<TalkVo>> getTalk(Long id) {
        List<TalkVo> talks = talkService.getTalks(id);
        return Resp.success(talks);
    }

    @GetMapping("/talk/single")
    @SaIgnore
    public Resp<TalkVo> getSingleTalk(Long id) {
        TalkVo singleTalk = talkService.getSingleTalk(id);
        return Resp.success(singleTalk);
    }

    @PostMapping("/talk")
    public Resp<Object> pubTalk(@RequestBody TalkParam talkParam) {
        boolean b = talkService.saveTalk(talkParam);
        if (b) {
            return Resp.success();
        } else {
            return Resp.failure("fail");
        }
    }

    @PostMapping("/talk/love")
    public Resp<String> loveTalk(@RequestBody TalkParam talkParam) {
        boolean b = talkService.loveTalk(talkParam);
        if (b) {
            return Resp.success("点赞成功");
        } else {
            return Resp.success("取消点赞成功");
        }
    }

    @DeleteMapping("/talk")
    public Resp<String> delTalk(@RequestBody TalkParam talkParam) {
        talkService.delTalk(talkParam);
        return Resp.success("删除成功");
    }

    @PostMapping("/talk/addFollow")
    public Resp<String> addFollow(@RequestBody TalkParam talkParam) {
        talkService.addFollow(talkParam);
        return Resp.success();
    }

    @PostMapping("/talk/subFollow")
    public Resp<String> subFollow(@RequestBody TalkParam talkParam) {
        talkService.subFollow(talkParam);
        return Resp.success();
    }

    @GetMapping("/talk/hot")
    @SaIgnore
    public Resp<List<TalkVo>> getHotTalk(Long id) {
        List<TalkVo> hotTalk = talkService.getHotTalk(id);
        return Resp.success(hotTalk);
    }

    @GetMapping("/talk/tag")
    public Resp<List<TalkVo>> getTalkByTag(Long id, String tag) {
        List<TalkVo> talkByTag = talkService.getTalkByTag(id, tag);
        return Resp.success(talkByTag);
    }
}
