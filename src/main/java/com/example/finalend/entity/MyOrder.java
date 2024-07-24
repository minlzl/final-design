package com.example.finalend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyOrder implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long voucherId;

    private Long userId;

    private LocalDateTime createTime;
}
