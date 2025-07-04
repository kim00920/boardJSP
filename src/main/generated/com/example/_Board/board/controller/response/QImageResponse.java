package com.example._Board.board.controller.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.example._Board.board.controller.response.QImageResponse is a Querydsl Projection type for ImageResponse
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QImageResponse extends ConstructorExpression<ImageResponse> {

    private static final long serialVersionUID = 1524963363L;

    public QImageResponse(com.querydsl.core.types.Expression<String> fileUrl) {
        super(ImageResponse.class, new Class<?>[]{String.class}, fileUrl);
    }

}

