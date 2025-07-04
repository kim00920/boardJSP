package com.example.boardtest.factory;

import com.example._Board.board.domain.Category;

public class CategoryFactory {

    public static Category createCategory1() {
        return Category.builder()
                .id(1L)
                .categoryName("스포츠")
                .build();
    }

    public static Category createCategory2() {
        return Category.builder()
                .id(2L)
                .categoryName("일상")
                .build();
    }
}
