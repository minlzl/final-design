package com.example.finalend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class History {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String ipAddr;

    private String type;

    private Long contentId;

    private LocalDateTime createTime;
}
