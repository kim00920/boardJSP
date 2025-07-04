package com.example._Board.comment.controller.response;

import com.example._Board.board.controller.response.ImageResponse;
import com.example._Board.board.domain.Board;
import com.example._Board.board.domain.Image;
import com.example._Board.comment.domain.Comment;
import com.example._Board.comment.domain.ReplyComment;
import com.example._Board.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponse {

    private Long id;
    private Long userId;
    private String userName;
    private Long boardId;
    private String comment;
    private LocalDateTime createdAt;
    private String createdAtStr;
    private List<ReplyCommentResponse> replyResponse;

    public CommentResponse(Comment comment) {
        this.id = comment.getId();
        this.userId = comment.getUser().getId(); // n + 1 발생 부분
        this.userName = comment.getUser().getName(); // n + 1 발생 부분
        this.boardId = comment.getBoard().getId(); // 특정 게시글 하나라서 1차 캐시에 저장되서 1번만 발생
        this.comment = comment.getComment();
        this.createdAt = comment.getCreatedAt();
        this.createdAtStr = formatDateTime(comment.getCreatedAt());
        this.replyResponse = comment.getReplyComments() // n + 1 발생 부분
                .stream()
                .map(ReplyCommentResponse::new).collect(Collectors.toList());
    }

    private String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public static List<CommentResponse> toCommentResponseList(List<Comment> comments) {
        return comments.stream()
                .map(CommentResponse::new)
                .collect(Collectors.toList());
    }

    public static CommentResponse toDto(Comment comment) {
        return CommentResponse.builder()
                .userId(comment.getUser().getId())
                .userName(comment.getUser().getName())
                .boardId(comment.getBoard().getId())
                .comment(comment.getComment())
                .createdAt(comment.getCreatedAt())
                .createdAtStr(comment.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .build();

    }



}

