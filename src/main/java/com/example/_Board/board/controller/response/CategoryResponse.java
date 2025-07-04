package com.example._Board.board.controller.response;

import com.example._Board.board.domain.Category;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CategoryResponse {

    private Long categoryId;
    private String categoryName;

    public static CategoryResponse toResponse(Category category) {
        return CategoryResponse.builder()
                .categoryId(category.getId())
                .categoryName(category.getCategoryName()).build();
    }
}