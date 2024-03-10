package com.example.finalend.vo;

import com.example.finalend.entity.Comment;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyCommentVo {

    private String content;

    private String ref;

    private LocalDateTime pubTime;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long refId;

    public static MyCommentVo of(Comment comment, String ref) {
        MyCommentVo myCommentVo = new MyCommentVo();
        myCommentVo.setContent(comment.getContent());
        myCommentVo.setPubTime(comment.getPubTime());
        myCommentVo.setRef(ref);
        myCommentVo.setRefId(comment.getTalkId());
        return myCommentVo;
    }
}
