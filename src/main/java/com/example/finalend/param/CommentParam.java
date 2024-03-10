package com.example.finalend.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentParam {

    private Long talkId;

    private Long userId;

    private Long commentId;

    private String content;
}
