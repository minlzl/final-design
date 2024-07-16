package com.example.finalend.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.finalend.entity.Comment;
import com.example.finalend.entity.User;
import com.example.finalend.exception.ApiException;
import com.example.finalend.mapper.CommentMapper;
import com.example.finalend.mapper.UserMapper;
import com.example.finalend.param.CommentParam;
import com.example.finalend.param.TalkParam;
import com.example.finalend.vo.CommentVo;
import com.example.finalend.vo.PageVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {

    @Resource
    CommentMapper commentMapper;

    @Resource
    TalkService talkService;

    @Resource
    UserMapper userMapper;

    public PageVo<CommentVo> getComments(Integer pageNum, Integer pageSize, Long id) {
        if (id == null) {
            throw new ApiException("参数错误");
        }
        Page<Comment> commentPage = commentMapper.selectPage(new Page<>(pageNum, pageSize), new QueryWrapper<Comment>().eq("talk_id", id).orderByDesc("pub_time"));
        List<Comment> records = commentPage.getRecords();
        List<CommentVo> commentVos = new ArrayList<>();
        for (Comment comment : records) {
            CommentVo commentVo = new CommentVo();
            BeanUtils.copyProperties(comment, commentVo);
            User user = userMapper.selectOne(new QueryWrapper<User>().eq("id", comment.getFromId()));
            if (user == null) {
                throw new ApiException("参数错误");
            }
            commentVo.setName(user.getName());
            commentVo.setAvatar(user.getAvatar());
            commentVos.add(commentVo);
        }
        return new PageVo<>(commentVos, commentPage.getSize(), commentPage.getCurrent(), commentPage.getTotal());
    }

    @Transactional
    public void saveComment(CommentParam commentParam) {
        Assert.notNull(commentParam, "参数错误");
        long loginId = StpUtil.getLoginIdAsLong();
        Comment comment = new Comment();
        comment.setTalkId(commentParam.getTalkId());
        comment.setFromId(loginId);
        comment.setContent(commentParam.getContent());
        comment.setPubTime(LocalDateTime.now());
        commentMapper.insert(comment);
        talkService.addCommentNumber(commentParam.getTalkId());
    }

    public long getTalkId(long commentId) {
        Comment comment = commentMapper.selectById(commentId);
        return comment.getTalkId();
    }
}
