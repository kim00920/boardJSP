package com.example.boardtest.board.service;

import com.example._Board.board.controller.request.CategoryCreateRequest;
import com.example._Board.board.controller.request.CategoryEditRequest;
import com.example._Board.board.controller.response.CategoryResponse;
import com.example._Board.board.domain.Category;
import com.example._Board.board.repository.BoardRepository;
import com.example._Board.board.repository.CategoryRepository;
import com.example._Board.board.service.Impl.CategoryServiceImpl;
import com.example._Board.user.repository.UserRepository;
import com.example.boardtest.factory.CategoryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("카테고리 테스트")
public class CategoryServiceTest {

    @InjectMocks
    CategoryServiceImpl categoryService;

    @Mock
    CategoryRepository categoryRepository;

    @Mock
    BoardRepository boardRepository;



    @Test
    @DisplayName("카테고리 생성")
    void createCategory() {
        Category category = CategoryFactory.createCategory1();

        CategoryCreateRequest categoryCreateRequest = CategoryCreateRequest.builder()
                .categoryName(category.getCategoryName())
                .build();

        categoryService.categoryCreate(categoryCreateRequest);

        verify(categoryRepository).save(any());

    }
    @Test
    @DisplayName("카테고리 수정")
    void categoryEditTest() {
        Category category = CategoryFactory.createCategory1();
        CategoryEditRequest request = CategoryEditRequest.builder()
                .categoryName("카테고리수정")
                .build();

        given(categoryRepository.findById(category.getId())).willReturn(Optional.of(category));


        categoryService.categoryEdit(category.getId(), request);


        Assertions.assertThat(category.getCategoryName()).isEqualTo("카테고리수정");

    }

    @Test
    @DisplayName("카테고리 삭제")
    void categoryDelete() {

        Category category = CategoryFactory.createCategory1();

        given(categoryRepository.findById(category.getId())).willReturn(Optional.of(category));
        assertThatCode(() -> boardRepository.findAllByCategory(category)).doesNotThrowAnyException();

        categoryService.categoryDelete(category.getId());

        verify(categoryRepository).delete(category);
    }

    @Test
    @DisplayName("카테고리 전체 조회")
    void categoryFindAll() {

        Category category1 = CategoryFactory.createCategory1();
        Category category2 = CategoryFactory.createCategory2();

        given(categoryRepository.findAll()).willReturn(List.of(category1, category2));

        List<CategoryResponse> categoryResponses = categoryService.categoryFindAll();

        assertThat(categoryResponses.size()).isEqualTo(2);
    }
}
