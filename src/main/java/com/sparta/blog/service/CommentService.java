package com.sparta.blog.service;

import com.sparta.blog.dto.CommentRequestDto;
import com.sparta.blog.dto.MessageResponseDto;
import com.sparta.blog.entity.*;
import com.sparta.blog.repository.BoardRepository;
import com.sparta.blog.repository.CommentRepository;
import com.sparta.blog.repository.LikeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final LikeRepository likeRepository;

    public CommentService(CommentRepository commentRepository, BoardRepository boardRepository, LikeRepository likeRepository) {
        this.commentRepository = commentRepository;
        this.boardRepository = boardRepository;
        this.likeRepository = likeRepository;
    }

    //생성
    @Transactional
    public ResponseEntity<String> createComment(CommentRequestDto commentRequestDto, User user) {
        Board board = boardRepository.findBoardById(commentRequestDto.getBoardId()).orElseThrow(() -> new IllegalArgumentException("선택한 게시글이 없습니다."));
        Comment comment = new Comment(commentRequestDto, user, board);
        commentRepository.save(comment);

        return ResponseEntity.status(HttpStatus.OK).body("댓글 작성 성공 ^.<");
    }

//    private Board findBoard(Long id) {
//        return boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("선택한 게시글이 없습니다."));
//    }

    private Comment findComment(Long id) {
        return commentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("선택한 댓글이 없습니다."));
    }


    //수정
    @Transactional
    public ResponseEntity<MessageResponseDto> updateComment(Long id, CommentRequestDto commentRequestDto, User user) {
        Comment comment = findComment(id);
        // 어드민 체크
        if (user.getRole().equals(UserRoleEnum.ADMIN)) {
            comment.update(commentRequestDto, user);
            MessageResponseDto message = new MessageResponseDto("관리자 권한 댓글 수정 성공", HttpStatus.OK.value());
            return ResponseEntity.status(HttpStatus.OK).body(message);
        }
            // 일반 유저일 때
            if (!comment.getUser().getUsername().equals(user.getUsername())) {
                throw new IllegalArgumentException("선생님의 댓글이 아닙니다.");
            }
            comment.update(commentRequestDto, user);
            MessageResponseDto message = new MessageResponseDto("댓글 수정 성공", HttpStatus.OK.value());
            return ResponseEntity.status(HttpStatus.OK).body(message);
        }


        //삭제
        public ResponseEntity<MessageResponseDto> deleteComment (Long id, User user){
            Comment comment = findComment(id);
            // 어드민 체크
            if (user.getRole().equals(UserRoleEnum.ADMIN)) {
                commentRepository.delete(comment);
                MessageResponseDto message = new MessageResponseDto("관리자 권한 댓글 수정 성공", HttpStatus.OK.value());
                return ResponseEntity.status(HttpStatus.OK).body(message);
            }

            // 일반 유저일 때
            if (!comment.getUser().getUsername().equals(user.getUsername())) {
                throw new IllegalArgumentException("선생님의 댓글이 아닙니다.");
            }
            commentRepository.delete(comment);
            MessageResponseDto message = new MessageResponseDto("댓글 삭제 성공", HttpStatus.OK.value());
            return ResponseEntity.status(HttpStatus.OK).body(message);
        }

    // 좋아요 기능 구현
    public ResponseEntity<MessageResponseDto> likeComment(Long id, User user) {
        Comment comment = findComment(id);
        Optional<Like> like = likeRepository.findByUserIdAndCommentId(user.getId(), id);
        if (like.isEmpty()) {
            likeRepository.save(new Like(user, comment));
            MessageResponseDto message = new MessageResponseDto(" 댓글 좋아요 성공" , HttpStatus.OK.value());
            return ResponseEntity.status(200).body(message);
        }
        likeRepository.delete(like.get());
        return ResponseEntity.status(200).body(new MessageResponseDto(" 댓글 좋아요 취소" , HttpStatus.OK.value()));
    }


    }
