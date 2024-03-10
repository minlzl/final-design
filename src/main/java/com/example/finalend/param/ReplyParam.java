package com.example.finalend.param;

import lombok.Data;

@Data
public class ReplyParam {

    private Long replyId;

    private Long commentId;

    private String content;
}
