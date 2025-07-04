package com.example._Board.comment.viewController;

import com.example._Board.comment.controller.request.CommentCreateRequest;
import com.example._Board.comment.controller.request.CommentEditRequest;
import com.example._Board.comment.controller.request.ReplyCommentCreateRequest;
import com.example._Board.comment.controller.request.ReplyCommentEditRequest;
import com.example._Board.comment.controller.response.CommentPageResponse;
import com.example._Board.comment.controller.response.CommentResponse;
import com.example._Board.comment.repository.CommentRepository;
import com.example._Board.comment.service.CommentService;
import com.example._Board.comment.service.ReplyCommentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ViewCommentController {

    private final CommentService commentService;
    private final ReplyCommentService replyCommentService;
    private final CommentRepository commentRepository;

    @PostMapping("/board/{boardId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public String commentCreate(@PathVariable Long boardId,
                                @ModelAttribute @Valid CommentCreateRequest request) {
        commentService.commentCreate(boardId, request);
        return "댓글이 등록되었습니다.";
    }

    // 대댓글 작성
    @PostMapping("/comment/{commentId}/replyComment")
    @ResponseStatus(HttpStatus.CREATED)
    public String replyCommentCreate(@PathVariable Long commentId,
                                     @ModelAttribute @Valid ReplyCommentCreateRequest request) {
        replyCommentService.ReplyCommentCreate(commentId, request);
        return "대댓글이 등록되었습니다.";
    }

    // 특정 게시글의 댓글 전체 조회 (페이징 가능)
    @GetMapping("/board/{boardId}/comments")
    public CommentPageResponse getComments(
            @PathVariable Long boardId,
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 8) Pageable pageable) {

        Page<CommentResponse> page = commentService.commentFindAllByBoard(boardId, pageable);

        long replyCount = commentRepository.replyCommentCount(boardId);
        long total = page.getTotalElements() + replyCount;

        return new CommentPageResponse(
                page.getContent(),
                total,
                page.getNumber(),
                page.getTotalPages(),
                page.hasNext(),
                page.hasPrevious()
        );
    }

    @PutMapping("/comment/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public void commentEdit(@PathVariable("commentId") Long commentId,
                            @RequestBody @Valid CommentEditRequest request) {
        commentService.commentEdit(commentId, request);
    }

    // 댓글 삭제
    @DeleteMapping("/comment/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void commentDelete(@PathVariable("commentId") Long commentId) {
        commentService.commentDelete(commentId);
    }

    // 대댓글 수정
    @PutMapping("/replyComment/{replyCommentId}")
    @ResponseStatus(HttpStatus.OK)
    public void replyCommentEdit(@PathVariable("replyCommentId") Long replyCommentId,
                                 @RequestBody @Valid ReplyCommentEditRequest request) {
        replyCommentService.ReplyCommentEdit(replyCommentId, request);
    }

    // 대댓글 삭제
    @DeleteMapping("/replyComment/{replyCommentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void replyCommentDelete(@PathVariable("replyCommentId") Long replyCommentId) {
        replyCommentService.ReplyCommentDelete(replyCommentId);
    }
}
