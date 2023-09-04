package com.sparta.blog.service;

import com.sparta.blog.dto.BoardRequestDto;
import com.sparta.blog.dto.BoardResponseDto;
import com.sparta.blog.entity.Board;
import com.sparta.blog.entity.Comment;
import com.sparta.blog.entity.User;
import com.sparta.blog.repository.BoardRepository;
import com.sparta.blog.security.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@Service
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
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
        Board board = boardRepository.findBoardById(id).orElseThrow(() -> new RuntimeException("게시물이 존재하지 않습니다"));
        return new BoardResponseDto(board);
    }

    //수정
    @Transactional
    public ResponseEntity<String> updateBoard(Long id, BoardRequestDto boardRequestDto, User user) {
        Board board = findBoard(id);
        if (!board.getUser().getUsername().equals(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("안돼");
        }
        board.update(boardRequestDto, user);
        return ResponseEntity.status(HttpStatus.OK).body("게시글 수정 성공 ^.<");
    }

    //삭제
    public ResponseEntity<String> deleteBoard(Long id, User user) {
        Board board = findBoard(id);

        if(!board.getUser().getUsername().equals(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("안돼");
        }
        boardRepository.delete(board);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("삭제완료");
    }

    //아이디값 검색
    private Board findBoard(Long id) {
        return boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("선택한 게시글이 없습니다."));
    }

}
