package com.example._Board.comment.controller.response;

import com.example._Board.comment.domain.ReplyComment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReplyCommentResponse {


    private Long id;
    private String replyComment;
    private Long userId;
    private String userName;
    private LocalDateTime createdAt;
    private String createdAtStr;

    public ReplyCommentResponse(ReplyComment replyComment) {
        this.id = replyComment.getId();
        this.userId = replyComment.getUser().getId();
        this.userName = replyComment.getUser().getName();
        this.replyComment = replyComment.getReplyComment();
        this.createdAt = replyComment.getCreatedAt();
        this.createdAtStr = formatDateTime(replyComment.getCreatedAt());
    }

    private String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

}
