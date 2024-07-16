package com.example.finalend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Article implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String title;

    private String contentHtml;

    private String contentMd;

    @TableField(value = "abstract")
    private String abs;

    private String cover;

    private LocalDateTime createTime;

    private Boolean open;

    @TableLogic(value = "0",delval = "1")
    @TableField("is_deleted")
    private Boolean deleted;
}
