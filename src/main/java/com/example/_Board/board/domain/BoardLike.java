package com.example._Board.board.domain;

import com.example._Board.user.domain.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "board_Like")
public class BoardLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public BoardLike(Long id, Board board, User user) {
        this.id = id;
        this.board = board;
        this.user = user;
    }

    // 좋아요 누르기
    public static BoardLike increaseBoardLike(Board board, User user) {
        return BoardLike.builder()
                .board(board)
                .user(user)
                .build();
    }


}
