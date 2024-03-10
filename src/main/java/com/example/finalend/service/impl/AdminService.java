package com.example.finalend.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.finalend.async.impl.AsyncServiceImpl;
import com.example.finalend.entity.Article;
import com.example.finalend.entity.Comment;
import com.example.finalend.entity.History;
import com.example.finalend.entity.User;
import com.example.finalend.exception.ApiException;
import com.example.finalend.mapper.CommentMapper;
import com.example.finalend.mapper.HistoryMapper;
import com.example.finalend.mapper.UserMapper;
import com.example.finalend.param.UserParam;
import com.example.finalend.vo.*;
import com.google.protobuf.Api;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {

    @Resource
    UserMapper userMapper;

    @Resource
    TalkService talkService;

    @Resource
    HistoryMapper historyMapper;

    @Resource
    ArticleService articleService;

    @Resource
    CommentMapper commentMapper;


    public User update(UserParam userParam) {
        long loginId = StpUtil.getLoginIdAsLong();
        User user = new User();
        user.setId(loginId);
        BeanUtils.copyProperties(userParam, user);
        System.out.println(user);
        userMapper.updateById(user);
        user = userMapper.selectById(user.getId());
        return user;
    }

    public PageVo<TalkVo> getTalks(Integer pageNum, Integer pageSize) {
        return talkService.getSelfTalks(pageNum, pageSize);
    }

    public PageVo<ArticleVo> getArticles(Integer pageNum, Integer pageSize) {
        return articleService.getSelfArticles(pageNum, pageSize);
    }

    public User getUser() {
        long loginId = StpUtil.getLoginIdAsLong();
        return userMapper.selectById(loginId);
    }

    public PageVo<HistoryVo> getHistory(Long pageNum, Long pageSize) {
        long loginId = StpUtil.getLoginIdAsLong();
        Page<History> historyPage = historyMapper.selectPage(new Page<>(pageNum, pageSize), new QueryWrapper<History>().eq("user_id", loginId));
        List<HistoryVo> historyVos = new ArrayList<>();
        for (History history : historyPage.getRecords()) {
            User user = userMapper.selectById(history.getUserId());
            String content = "";
            switch (history.getType()) {
                case "talk":
                    TalkVo singleTalk = talkService.getSingleTalk(history.getContentId());
                    content = singleTalk.getContent();
                    break;
                case "article":
                    ArticleVo article = articleService.getArticle(history.getContentId());
                    content = article.getAbs();
                    break;
                default:
                    throw new ApiException("error");
            }
            historyVos.add(HistoryVo.of(history, user.getName(), content));
        }
        return new PageVo<>(historyVos, pageSize, pageNum, historyPage.getTotal());
    }

    public PageVo<MyCommentVo> getMyComments(Integer pageNum, Integer pageSize) {
        long loginId = StpUtil.getLoginIdAsLong();
        List<MyCommentVo> myCommentVos = new ArrayList<>();
        Page<Comment> commentPage = commentMapper.selectPage(new Page<>(pageNum, pageSize), new QueryWrapper<Comment>().eq("from_id", loginId));
        for (Comment comment : commentPage.getRecords()) {
            TalkVo talk = talkService.getSingleTalk(comment.getTalkId());
            myCommentVos.add(MyCommentVo.of(comment, talk.getContent()));
        }
        return new PageVo<>(myCommentVos, commentPage.getSize(), commentPage.getCurrent(), commentPage.getTotal());
    }
}
