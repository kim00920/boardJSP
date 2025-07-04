package com.example._Board.comment.controller.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReplyCommentEditRequest {

    @NotEmpty(message = "대댓글 내용을 입력해주세요")
    private String replyComment;
}
