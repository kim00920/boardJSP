package com.example.boardtest.factory;

import com.example._Board.board.domain.Board;
import com.example._Board.comment.domain.Comment;
import com.example._Board.user.domain.User;

public class CommentFactory {

    public static Comment createComment(Board board, User user) {
        return Comment.builder()
                .id(1L)
                .comment("댓글")
                .board(board)
                .user(user)
                .build();

    }
}
