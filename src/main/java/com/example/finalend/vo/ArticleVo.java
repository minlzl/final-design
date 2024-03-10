package com.example.finalend.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.example.finalend.entity.Article;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ArticleVo {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    private String name;

    private String avatar;

    private String title;

    private String contentHtml;

    private String contentMd;

    private String abs;

    private String cover;

    private List<String> tags;

    private LocalDateTime createTime;

    public static ArticleVo of(Article article) {
        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article, articleVo);
        return articleVo;
    }
}
