package com.example._Board.comment.controller.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentCreateRequest {

    @NotEmpty(message = "댓글을 입력하세요")
    @Size(max = 100, message = "최대 100 글자까지만 작성할 수 있습니다")
    private String comment;
}
