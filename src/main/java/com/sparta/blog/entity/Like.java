package com.sparta.blog.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "likes")
@NoArgsConstructor
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = true)
    private Board board;

    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = true)
    private Comment comment;

    public Like(User user, Board board, Comment comment) {
        this.user = user;
        this.board = board;
        this.comment = comment;
    }

    public Like(User user, Board board) {
        this.user = user;
        this.board = board;
    }

    public Like(User user, Comment comment) {
        this.user = user;
        this.comment = comment;
    }
}