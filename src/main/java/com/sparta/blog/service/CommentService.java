package com.sparta.blog.service;

import com.sparta.blog.dto.CommentRequestDto;
import com.sparta.blog.entity.Board;
import com.sparta.blog.entity.Comment;
import com.sparta.blog.entity.User;
import com.sparta.blog.entity.UserRoleEnum;
import com.sparta.blog.repository.BoardRepository;
import com.sparta.blog.repository.CommentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    public CommentService(CommentRepository commentRepository, BoardRepository boardRepository) {
        this.commentRepository = commentRepository;
        this.boardRepository = boardRepository;
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
    public ResponseEntity<String> updateComment(Long id, CommentRequestDto commentRequestDto, User user) {
        Comment comment = findComment(id);
        // 어드민 체크
        if (user.getRole().equals(UserRoleEnum.ADMIN)) {
            comment.update(commentRequestDto,user);
            return ResponseEntity.status(200).body("상태코드 : " + HttpStatus.OK.value() + " 메세지 : 관리자 권한 댓글 수정 성공"); }

        // 일반 유저일 때
        if (!comment.getUser().getUsername().equals(user.getUsername())) {
            return ResponseEntity.status(400).body("상태코드 : " + HttpStatus.BAD_REQUEST.value() + " 메세지 : 선생님 댓글이 아닙니다.");}
            comment.update(commentRequestDto,user);
        return ResponseEntity.status(200).body("상태코드 : " + HttpStatus.OK.value() + " 메세지 : 댓글 수정 성공");
    }


    //삭제
    public ResponseEntity<String> deleteComment(Long id, User user) {
        Comment comment = findComment(id);
        // 어드민 체크
        if (user.getRole().equals(UserRoleEnum.ADMIN)) {
            commentRepository.delete(comment);
            return ResponseEntity.status(200).body("상태코드 : " + HttpStatus.OK.value() + " 메세지 : 관리자 권한 댓글 삭제 성공"); }

        // 일반 유저일 때
        if (!comment.getUser().getUsername().equals(user.getUsername())) {
            return ResponseEntity.status(400).body("상태코드 : " + HttpStatus.BAD_REQUEST.value() + " 메세지 : 선생님 댓글이 아닙니다.");}
        commentRepository.delete(comment);
        return ResponseEntity.status(200).body("상태코드 : " + HttpStatus.OK.value() + " 메세지 : 댓글 삭제 성공");
    }




}
