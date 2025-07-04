package com.example._Board.board.controller.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.example._Board.board.controller.response.QBoardPageResponse is a Querydsl Projection type for BoardPageResponse
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QBoardPageResponse extends ConstructorExpression<BoardPageResponse> {

    private static final long serialVersionUID = 870738909L;

    public QBoardPageResponse(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<Long> userId, com.querydsl.core.types.Expression<String> title, com.querydsl.core.types.Expression<String> content, com.querydsl.core.types.Expression<String> categoryName, com.querydsl.core.types.Expression<Boolean> notice, com.querydsl.core.types.Expression<Integer> commentCount, com.querydsl.core.types.Expression<Integer> viewCount, com.querydsl.core.types.Expression<Integer> likeCount, com.querydsl.core.types.Expression<java.time.LocalDateTime> createdAt, com.querydsl.core.types.Expression<Boolean> existImage) {
        super(BoardPageResponse.class, new Class<?>[]{long.class, long.class, String.class, String.class, String.class, boolean.class, int.class, int.class, int.class, java.time.LocalDateTime.class, boolean.class}, id, userId, title, content, categoryName, notice, commentCount, viewCount, likeCount, createdAt, existImage);
    }

}

