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
@NoArgsConstructor
@Table(name = "comment")
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

//    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
//    private List<Like> likesList = new ArrayList<>();

    public Comment(CommentRequestDto commentRequestDto, User user, Board board) {
        this.comment = commentRequestDto.getComment();
        this.user = user;
        this.board = board;
    }

    public void update(CommentRequestDto commentRequestDto, User user) {
        this.comment = commentRequestDto.getComment();
        this.user = user;
    }


}
