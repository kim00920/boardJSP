package com.example._Board.board.controller.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardPageResponse {

    private Long id;
    private Long userId;
    private String title;
    private String content;
    private String categoryName;
    private boolean notice;
    private int commentCount;
    private int viewCount;
    private int likeCount;
    private LocalDateTime createdAt;

    private String formattedCreatedAt;
    private boolean existImage;

    @QueryProjection
    public BoardPageResponse(Long id, Long userId, String title, String content, String categoryName, boolean notice,
                             int commentCount, int viewCount, int likeCount, LocalDateTime createdAt, boolean existImage) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.categoryName = categoryName;
        this.notice = notice;
        this.commentCount = commentCount;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.createdAt = createdAt;

        this.formattedCreatedAt = createdAt.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.existImage = existImage;
    }


}
