package com.sparta.blog.dto;

import lombok.Getter;

@Getter
public class CommentRequestDto {
    private Long board_id;
    private String comment;
}
