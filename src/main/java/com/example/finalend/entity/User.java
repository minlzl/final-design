package com.example.finalend.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("user")
public class User implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String account;

    private String password;

    private String name;

    private String email;

    private String avatar;

    private String description;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @TableLogic(value = "0",delval = "1")
    @TableField("is_deleted")
    private Boolean deleted;

    private Byte role;
}
