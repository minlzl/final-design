package com.example.finalend.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class CommentVo implements Serializable {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private Long talkId;

    private String name;

    private String avatar;

    private LocalDateTime pubTime;

    private String content;
}
