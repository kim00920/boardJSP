package com.example._Board.comment.controller;

import io.swagger.v3.oas.annotations.Operation;
import com.example._Board.comment.controller.request.CommentCreateRequest;
import com.example._Board.comment.controller.request.CommentEditRequest;
import com.example._Board.comment.controller.response.CommentResponse;
import com.example._Board.comment.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 생성")
    @PostMapping("/board/{boardId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public void commentCreate(@PathVariable("boardId") Long boardId,
                              @RequestBody @Valid CommentCreateRequest request) {
        commentService.commentCreate(boardId, request);
    }


    @Operation(summary = "특정 게시글의 댓글 전체 조회")
    @GetMapping("/board/{boardId}/comment")
    @ResponseStatus(HttpStatus.OK)
    public Page<CommentResponse> commentFindAllByBoard(@PathVariable("boardId") Long boardId,
                                                @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return commentService.commentFindAllByBoard(boardId, pageable);
    }

    @Operation(summary = "댓글 전체 조회")
    @GetMapping("/comment/all")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentResponse> commentFindAll() {
        return commentService.commentFindAll();
    }

    @Operation(summary = "댓글 단건 조회")
    @GetMapping("/comment/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentResponse commentFindOne(@PathVariable("commentId") Long commentId) {
        return commentService.commentFindOne(commentId);
    }


    @Operation(summary = "댓글 수정")
    @PutMapping("/comment/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public void commentEdit(@PathVariable("commentId") Long commentId,
                            @RequestBody @Valid CommentEditRequest request) {
        commentService.commentEdit(commentId, request);
    }

    // 댓글 삭제
    @Operation(summary = "댓글 삭제")
    @DeleteMapping("/comment/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void commentDelete(@PathVariable("commentId") Long commentId) {
        commentService.commentDelete(commentId);
    }
}
