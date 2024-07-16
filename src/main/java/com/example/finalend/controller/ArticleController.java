package com.example.finalend.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import com.example.finalend.annotation.AccessLimit;
import com.example.finalend.annotation.ApiRestController;
import com.example.finalend.entity.Article;
import com.example.finalend.service.impl.ArticleService;
import com.example.finalend.vo.ArticleVo;
import com.example.finalend.vo.PageVo;
import com.example.finalend.vo.Resp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@ApiRestController
@SaCheckLogin
public class ArticleController {

    @Resource
    ArticleService articleService;

    @PostMapping("/article")
    public Resp<Article> saveArticle(@RequestBody Article article) {
        article = articleService.save(article);
        return Resp.success(article);
    }

    @GetMapping("/articles")
    @SaIgnore
    @AccessLimit(key = "article", timeout = 500)
    public Resp<PageVo<ArticleVo>> getArticle(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                            @RequestParam(value = "search", defaultValue = "") String search) {
        PageVo<ArticleVo> articles = articleService.getArticles(pageNum, pageSize, search);
        return Resp.success(articles);
    }

    @GetMapping("/article")
    @SaIgnore
    public Resp<ArticleVo> getSingleArticle(Long id) {
        ArticleVo articleVo = articleService.getArticle(id);
        return Resp.success(articleVo);
    }
}
