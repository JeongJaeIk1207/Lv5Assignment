package com.sparta.blog.service;

import com.sparta.blog.dto.BoardRequestDto;
import com.sparta.blog.dto.BoardResponseDto;
import com.sparta.blog.dto.MessageResponseDto;
import com.sparta.blog.entity.Board;
import com.sparta.blog.entity.Like;
import com.sparta.blog.entity.User;
import com.sparta.blog.entity.UserRoleEnum;
import com.sparta.blog.repository.BoardRepository;
import com.sparta.blog.repository.LikeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final LikeRepository likeRepository;

    public BoardService(BoardRepository boardRepository, LikeRepository likeRepository) {
        this.boardRepository = boardRepository;
        this.likeRepository = likeRepository;
    }

    //조회
    public List<BoardResponseDto> getBoard() {
        return boardRepository.findAllByOrderByModifiedAtDesc().stream().map(BoardResponseDto::new).toList();
    }

    //생성
    @Transactional
    public BoardResponseDto createBoard(BoardRequestDto boardRequestDto, User user) {

        Board board = new Board(boardRequestDto, user);
        Board saveBoard = boardRepository.save(board);
        BoardResponseDto boardResponseDto = new BoardResponseDto(saveBoard);

        return boardResponseDto;
    }

    public BoardResponseDto getBoardById(Long id) {
        Board board = boardRepository.findBoardById(id).orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다"));
        return new BoardResponseDto(board);
    }

    //수정
    @Transactional
    public ResponseEntity<MessageResponseDto> updateBoard(Long id, BoardRequestDto boardRequestDto, User user) {
        Board board = findBoard(id);

        // 어드민 체크
        if (user.getRole().equals(UserRoleEnum.ADMIN)) {
            board.update(boardRequestDto, user);
            MessageResponseDto message = new MessageResponseDto("관리자 권한 게시물 수정 성공", HttpStatus.OK.value());
            return ResponseEntity.status(HttpStatus.OK).body(message);
        }

        // 일반 유저일 때
        if (!board.getUser().getUsername().equals(user.getUsername())) {
            throw new IllegalArgumentException("선생님의 게시글이 아닙니다.");
        }
        board.update(boardRequestDto, user);
        MessageResponseDto message = new MessageResponseDto("게시글 수정 성공", HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    //삭제
    public ResponseEntity<MessageResponseDto> deleteBoard(Long id, User user) {
        Board board = findBoard(id);

        // 어드민 체크
        if (user.getRole().equals(UserRoleEnum.ADMIN)) {
            boardRepository.delete(board);
            MessageResponseDto message = new MessageResponseDto("관리자 권한 게시물 삭제 성공", HttpStatus.OK.value());
            return ResponseEntity.status(HttpStatus.OK).body(message);
        }
        // 일반 유저일 때
        if (!board.getUser().getUsername().equals(user.getUsername())) {
            throw new IllegalArgumentException("선생님의 게시글이 아닙니다.");
        }
        boardRepository.delete(board);
        MessageResponseDto message = new MessageResponseDto("게시글 삭제 성공", HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    //아이디값 검색
    private Board findBoard(Long id) {
        return boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("선택한 게시글이 없습니다."));
    }

    // 좋아요 기능 구현
    public ResponseEntity<MessageResponseDto> likeBoard(Long id, User user) {
        Board board = findBoard(id);
        Optional<Like> like = likeRepository.findByUserIdAndBoardId(user.getId(), id);
        if (like.isEmpty()) {
            likeRepository.save(new Like(user, board));
            MessageResponseDto message = new MessageResponseDto(" 게시물 좋아요 성공" , HttpStatus.OK.value());
            return ResponseEntity.status(200).body(message);
        }
        likeRepository.delete(like.get());
        return ResponseEntity.status(200).body(new MessageResponseDto(" 게시물 좋아요 취소" , HttpStatus.OK.value()));
    }
}


