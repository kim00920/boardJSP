package com.example._Board.board.service.Impl;

import com.example._Board.board.controller.request.CategoryCreateRequest;
import com.example._Board.board.controller.response.CategoryResponse;
import com.example._Board.board.domain.Category;
import com.example._Board.board.controller.request.CategoryEditRequest;
import com.example._Board.board.repository.BoardRepository;
import com.example._Board.board.repository.CategoryRepository;
import com.example._Board.board.service.CategoryService;
import com.example._Board.error.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.example._Board.error.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final BoardRepository boardRepository;


    // 카테고리 생성
    @Override
     public void categoryCreate(CategoryCreateRequest categoryCreateRequest) {
        if (categoryRepository.findByCategoryName(categoryCreateRequest.getCategoryName()).isPresent()) {
            throw new BusinessException(CATEGORY_NAME_DUPLICATED);
        }
        Category category = Category.toCategory(categoryCreateRequest);
        categoryRepository.save(category);
    }

    // 카테고리 삭제 (관련 게시글이 존재하면 삭제 X)
    @Override
    public void categoryDelete(Long categoryId) {
        Category category = ExistCategoryCheck(categoryId);


        if (!boardRepository.findAllByCategory(category).isEmpty()) {
            throw new BusinessException(CATEGORY_EXIST_BOARD);
        }
            categoryRepository.delete(category);

    }

    // 카테고리 전체 조회
    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> categoryFindAll() {
        return categoryRepository.findAll()
                .stream().map(CategoryResponse::toResponse)
                .collect(Collectors.toList());
    }

    // 카테고리 단건 조회
    @Override
    @Transactional(readOnly = true)
    public CategoryResponse categoryFindOne(Long categoryId) {
       Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new BusinessException(NOT_FOUND_CATEGORY));

       return CategoryResponse.toResponse(category);
    }

    // 카테고리 수정
    @Override
    public void categoryEdit(Long categoryId, CategoryEditRequest categoryEditRequest) {
        Category category = ExistCategoryCheck(categoryId);
        category.editCategory(categoryEditRequest);
    }


    // 카테고리가 존재하는지 확인하는 메서드
    private Category ExistCategoryCheck(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BusinessException(NOT_FOUND_CATEGORY));
    }
}
