package com.example._Board.board.service;

import com.example._Board.board.controller.request.CategoryCreateRequest;
import com.example._Board.board.controller.response.CategoryResponse;
import com.example._Board.board.controller.request.CategoryEditRequest;

import java.util.List;

public interface CategoryService {

    // 카테고리 생성
    void categoryCreate(CategoryCreateRequest categoryCreateRequest);

    // 카테고리 삭제
    void categoryDelete(Long categoryId);

    // 카테고리 전체 조회
    List<CategoryResponse> categoryFindAll();

    // 카테고리 단건 조회
    CategoryResponse categoryFindOne(Long categoryId);

    // 카테고리 수정
    void categoryEdit(Long categoryId, CategoryEditRequest categoryEditRequest);



}
