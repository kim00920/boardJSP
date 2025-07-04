package com.example._Board.comment.controller;

import io.swagger.v3.oas.annotations.Operation;
import com.example._Board.comment.controller.request.ReplyCommentCreateRequest;
import com.example._Board.comment.controller.request.ReplyCommentEditRequest;
import com.example._Board.comment.service.ReplyCommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class ReplyCommentController {

    private final ReplyCommentService replyCommentService;

    // 대댓글 생성
    @Operation(summary = "대댓글 생성")
    @PostMapping("/comment/{commentId}/replyComment")
    @ResponseStatus(HttpStatus.CREATED)
    public void replyCommentCreate(@PathVariable("commentId") Long commentId,
                                   @RequestBody @Valid ReplyCommentCreateRequest request) {
        replyCommentService.ReplyCommentCreate(commentId, request);
    }

    // 대댓글 수정
    @Operation(summary = "대댓글 수정")
    @PutMapping("/replyComment/{replyCommentId}")
    @ResponseStatus(HttpStatus.OK)
    public void replyCommentEdit(@PathVariable("replyCommentId") Long replyCommentId,
                                 @RequestBody @Valid ReplyCommentEditRequest request) {
        replyCommentService.ReplyCommentEdit(replyCommentId, request);
    }

    // 대댓글 삭제
    @Operation(summary = "대댓글 삭제")
    @DeleteMapping("/replyComment/{replyCommentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void replyCommentDelete(@PathVariable("replyCommentId") Long replyCommentId) {
        replyCommentService.ReplyCommentDelete(replyCommentId);
    }
}
