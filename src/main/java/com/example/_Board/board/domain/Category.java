package com.example._Board.board.domain;

import com.example._Board.board.controller.request.CategoryCreateRequest;
import com.example._Board.board.controller.request.CategoryEditRequest;
import com.example._Board.config.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "category")
@Entity
public class Category extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(nullable = false, unique = true)
    private String categoryName;

    @Builder
    public Category(Long id, String categoryName) {
        this.id = id;
        this.categoryName = categoryName;
    }

    public static Category toCategory(CategoryCreateRequest categoryCreateRequest) {
        return Category.builder()
                .categoryName(categoryCreateRequest.getCategoryName())
                .build();
    }

    public void editCategory(CategoryEditRequest categoryEditRequest) {
        this.categoryName = categoryEditRequest.getCategoryName();

    }
}

