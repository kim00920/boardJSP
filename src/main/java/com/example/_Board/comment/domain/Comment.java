package com.example._Board.comment.domain;

import com.example._Board.board.domain.Board;
import com.example._Board.comment.controller.request.CommentCreateRequest;
import com.example._Board.comment.controller.request.CommentEditRequest;
import com.example._Board.config.BaseTimeEntity;
import com.example._Board.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "comment")
@Entity
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Column(nullable = false)
    private String comment;     //내용

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 100)
    private List<ReplyComment> replyComments = new ArrayList<>();

    @Builder
    public Comment(Long id, User user, Board board, String comment) {
        this.id = id;
        this.user = user;
        this.board = board;
        this.comment = comment;
    }

    // 연관관계
    public void setBoard(Board board) {
        this.board = board;
        board.getComments().add(this);
    }

    // 댓글 생성
    public static Comment createComment(User user, Board board, CommentCreateRequest commentCreateRequest) {
        return Comment.builder()
                .user(user)
                .board(board)
                .comment(commentCreateRequest.getComment())
                .build();
    }

    // 댓글 수정
    public void editComment(CommentEditRequest commentEditRequest) {
        this.comment = commentEditRequest.getComment();
    }
}
