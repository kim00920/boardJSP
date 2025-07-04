package com.example._Board.comment.controller.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReplyCommentCreateRequest {

    @NotEmpty(message = "대댓글 내용을 입력해주세요")
    private String replyComment;
}
