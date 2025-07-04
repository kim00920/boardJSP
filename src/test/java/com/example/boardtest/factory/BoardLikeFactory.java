package com.example.boardtest.factory;

import com.example._Board.board.domain.Board;
import com.example._Board.board.domain.BoardLike;
import com.example._Board.user.domain.User;

public class BoardLikeFactory {
    public static BoardLike createBoardLike(User user, Board board) {
        return BoardLike.builder()
                .id(1L)
                .user(user)
                .board(board)
                .build();
    }
}
