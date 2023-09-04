package com.sparta.blog.entity;

import com.sparta.blog.dto.BoardRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "board")
@NoArgsConstructor
public class Board extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "contents", nullable = false, length = 500)
    private String contents;
    @Column(name = "title")
    private String title;

    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private List<Comment> commentsList = new ArrayList<>();


    public Board(BoardRequestDto boardrequestDto, User user) {
        this.contents = boardrequestDto.getContents();
        this.title = boardrequestDto.getTitle();
        this.user = user;
    }

    public void update(BoardRequestDto boardrequestDto, User user) {
        this.contents = boardrequestDto.getContents();
        this.title = boardrequestDto.getTitle();
        this.user = user;
    }
}
