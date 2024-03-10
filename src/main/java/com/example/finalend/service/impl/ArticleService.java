package com.example.finalend.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.finalend.annotation.HistoryLog;
import com.example.finalend.entity.Article;
import com.example.finalend.exception.ApiException;
import com.example.finalend.mapper.ArticleMapper;
import com.example.finalend.mapper.UserMapper;
import com.example.finalend.vo.ArticleVo;
import com.example.finalend.vo.PageVo;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ArticleService {

    @Resource
    ArticleMapper articleMapper;

    @Resource
    UserMapper userMapper;

    public Article save(Article article) {
        long loginId = StpUtil.getLoginIdAsLong();
        article.setUserId(loginId);
        article.setCreateTime(LocalDateTime.now());
        articleMapper.insert(article);
        return article;
    }

    public PageVo<ArticleVo> getArticles(Integer pageNum, Integer pageSize, String search) {
        Page<Article> articlePage = articleMapper.selectPage(new Page<>(pageNum, pageSize), new QueryWrapper<Article>().like("abstract", search).orderByDesc("create_time"));
        return getArticleVos(articlePage);
    }

    @Cacheable(value = "article", key = "#id")
    @HistoryLog(type = "article")
    public ArticleVo getArticle(Long id) {
        Article article = articleMapper.selectById(id);
        if (article == null) {
            throw new ApiException("参数错误");
        }
        return ArticleVo.of(article);
    }

    private PageVo<ArticleVo> getArticleVos(Page<Article> articlePage) {
        List<Article> records = articlePage.getRecords();
        List<ArticleVo> articleVos = new ArrayList<>();
        for (Article article : records) {
            articleVos.add(ArticleVo.of(article));
        }
        return new PageVo<>(articleVos, articlePage.getSize(), articlePage.getCurrent(), articlePage.getTotal());
    }

    public PageVo<ArticleVo> getSelfArticles(Integer pageNum, Integer pageSize) {
        long loginId = StpUtil.getLoginIdAsLong();
        Page<Article> articlePage = articleMapper.selectPage(new Page<>(pageNum, pageSize), new QueryWrapper<Article>().eq("user_id", loginId).orderByDesc("create_time"));
        return getArticleVos(articlePage);
    }
}
