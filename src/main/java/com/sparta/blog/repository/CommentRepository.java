package com.sparta.blog.repository;

import com.sparta.blog.entity.Board;
import com.sparta.blog.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
//    Optional<Comment> findCommentById(Long id);
}
