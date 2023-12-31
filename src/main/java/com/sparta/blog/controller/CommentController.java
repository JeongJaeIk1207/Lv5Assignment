package com.sparta.blog.controller;

import com.sparta.blog.dto.CommentRequestDto;
import com.sparta.blog.dto.MessageResponseDto;
import com.sparta.blog.entity.Board;
import com.sparta.blog.repository.BoardRepository;
import com.sparta.blog.security.UserDetailsImpl;
import com.sparta.blog.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    //댓글 작성
    @PostMapping("/comment")
    public ResponseEntity<String> createComment(
            @RequestBody CommentRequestDto commentRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.createComment(commentRequestDto, userDetails.getUser());
    }

    //수정
    @PutMapping("/comment/{id}")
    public ResponseEntity<MessageResponseDto> updateComment(@PathVariable Long id, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.updateComment(id, commentRequestDto, userDetails.getUser());
    }

    // 삭제
    @DeleteMapping("/comment/{id}")
    public ResponseEntity<MessageResponseDto> deleteComment(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.deleteComment(id, userDetails.getUser());
    }

    // 좋아요
    @PutMapping("/comment/{id}/like")
    public ResponseEntity<MessageResponseDto> likeComment(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.likeComment(id, userDetails.getUser());
    }



}
