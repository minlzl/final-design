package com.example.finalend.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.finalend.entity.Reply;
import com.example.finalend.entity.User;
import com.example.finalend.exception.ApiException;
import com.example.finalend.mapper.ReplyMapper;
import com.example.finalend.mapper.UserMapper;
import com.example.finalend.param.CommentParam;
import com.example.finalend.param.ReplyParam;
import com.example.finalend.vo.PageVo;
import com.example.finalend.vo.ReplyVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReplyService {

    @Resource
    ReplyMapper replyMapper;

    @Resource
    UserMapper userMapper;

    @Resource
    CommentService commentService;

    @Resource
    TalkService talkService;

    public PageVo<ReplyVo> getReplies(Integer pageNum, Integer pageSize, Long commentId) {
        if (commentId == null)
            throw new ApiException("参数错误");
        Page<Reply> replyPage = replyMapper.selectPage(new Page<>(pageNum, pageSize), new QueryWrapper<Reply>().eq("comment_id", commentId));
        List<Reply> records = replyPage.getRecords();
        List<ReplyVo> replyVos = new ArrayList<>();
        for (Reply reply : records) {
            replyVos.add(ReplyVo.of(reply, userMapper));
        }
        return new PageVo<>(replyVos, replyPage.getSize(), replyPage.getCurrent(), replyPage.getTotal());
    }

    @Transactional
    public ReplyVo saveReply(ReplyParam replyParam) {
        Assert.notNull(replyParam, "参数错误");
        long loginId = StpUtil.getLoginIdAsLong();
        Reply reply = new Reply();
        reply.setCommentId(replyParam.getCommentId());
        reply.setPubTime(LocalDateTime.now());
        reply.setContent(replyParam.getContent());
        reply.setFromId(loginId);
        replyMapper.insert(reply);
        long talkId = commentService.getTalkId(reply.getCommentId());
        talkService.addCommentNumber(talkId);
        return ReplyVo.of(reply, userMapper);
    }
}
