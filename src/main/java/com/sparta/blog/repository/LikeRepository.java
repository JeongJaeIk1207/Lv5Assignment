package com.sparta.blog.repository;

import com.sparta.blog.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserIdAndBoardId(Long userId, Long boardId);
}
