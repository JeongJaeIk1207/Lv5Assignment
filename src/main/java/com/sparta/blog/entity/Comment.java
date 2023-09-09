package com.sparta.blog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.blog.dto.CommentRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "comment")
@NoArgsConstructor
public class Comment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "comment", nullable = false, length = 500)
    private String comment;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE)
    private List<Like> likessList = new ArrayList<>();

    public Comment(CommentRequestDto commentRequestDto, User user, Board board) {
        this.user = user;
        this.board = board;
        this.comment = commentRequestDto.getComment();
    }

    public void update(CommentRequestDto commentRequestDto, User user) {
        this.user = user;
        this.comment = commentRequestDto.getComment();
    }
}