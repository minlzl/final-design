package com.example.finalend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Comment implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long talkId;

    private String content;

    private Long fromId;

    private Long toId;

    private LocalDateTime pubTime;

    @TableLogic(value = "0", delval = "1")
    @TableField("is_deleted")
    private Boolean deleted;
}
