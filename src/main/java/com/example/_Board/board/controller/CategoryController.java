package com.example._Board.board.controller;

import com.example._Board.board.controller.response.CategoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import com.example._Board.board.controller.request.CategoryCreateRequest;
import com.example._Board.board.controller.request.CategoryEditRequest;
import com.example._Board.board.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/category")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void categoryCreate(@RequestBody @Valid CategoryCreateRequest categoryCreateRequest) {
        categoryService.categoryCreate(categoryCreateRequest);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryResponse> findAllCategory() {
        return categoryService.categoryFindAll();
    }

    @GetMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryResponse findOneCategory(@PathVariable("categoryId")Long categoryId) {
       return categoryService.categoryFindOne(categoryId);
    }

    @PutMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public void categoryEdit(@PathVariable("categoryId") Long categoryId,
                             @RequestBody CategoryEditRequest categoryEditRequest) {
        categoryService.categoryEdit(categoryId, categoryEditRequest);
    }

    @Operation(summary = "카테고리 삭제")
    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public void categoryDelete(@PathVariable("categoryId") Long categoryId) {
        categoryService.categoryDelete(categoryId);
    }
}
