package com.example._Board.comment.controller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
public class CommentPageResponse {
    private List<CommentResponse> content; // 댓글 목록
    private long totalCount;               // 댓글 + 대댓글 총합
    private int currentPage;
    private int totalPages;
    private boolean hasNext;
    private boolean hasPrevious;
}