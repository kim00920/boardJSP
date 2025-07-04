package com.example._Board.board.controller.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryEditRequest {

    @NotEmpty(message = "카테고리는 필수 값 입니다.")
    private String categoryName;

}
