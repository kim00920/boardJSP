package com.example.boardtest.factory;

import com.example._Board.board.domain.Board;
import com.example._Board.comment.domain.Comment;
import com.example._Board.comment.domain.ReplyComment;
import com.example._Board.user.domain.User;

public class ReplyCommentFactory {

    public static ReplyComment createReplyComment(User user, Comment comment) {
        return ReplyComment.builder()
                .id(1L)
                .userId(user.getId())
                .comment(comment)
                .replyComment("대댓글")
                .build();
    }

    public static Comment createComment(User user, Board board) {
        return Comment.builder()
                .id(1L)
                .user(user)
                .board(board)
                .comment("댓글")
                .build();
    }
}
