package com.sparta.blog.dto;

import lombok.Getter;

@Getter
public class CommentRequestDto {
    private Long boardId;
    private String comment;
}
