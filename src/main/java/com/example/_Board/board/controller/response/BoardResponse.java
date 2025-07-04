package com.example._Board.board.controller.response;

import com.example._Board.board.domain.Board;
import com.example._Board.comment.controller.response.CommentResponse;
import com.example._Board.comment.domain.ReplyComment;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardResponse {

    private Long id;
    private Long userId;
    private String userName;
    private String title;
    private String content;
    private String categoryName;
    private boolean notice;
    private int commentCount;
    private int viewCount;
    private int likeCount;
    private LocalDateTime createdAt;
    private List<ImageResponse> imageList;
    private List<CommentResponse> commentList;

    private String formattedCreatedAt;
    private boolean existImage;



    // 게시글 전체 조회 (댓글 제외)
    public BoardResponse(Board board) {
        this.id = board.getId();
        this.userId = board.getUser().getId();
        this.userName = board.getUser().getName();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.notice = board.isNotice();
        this.viewCount = board.getViewCount();
        this.likeCount = board.getLikeCount();
        this.createdAt = board.getCreatedAt();
        this.categoryName = board.getCategory().getCategoryName();
        this.imageList = ImageResponse.toResponse(board);
        this.existImage = board.getImages() != null && !board.getImages().isEmpty();

        System.out.println("게시글 전체 조회 이미지 확인 : " + existImage);
    }


    // 매개변수를 받는 생성자 추가
    public BoardResponse( Boolean notice, int likeCount) {
        this.notice = notice;
        this.likeCount = likeCount;
    }

    // 특정 게시글의 모든 정보 (S3 이미지 + 카테고리 + 댓글 + 대댓글)
    public static BoardResponse toBoardResponse(Board board) {
        List<ImageResponse> images = ImageResponse.toResponse(board);
        return BoardResponse.builder()
                .id(board.getId())
                .userId(board.getUser().getId())
                .userName(board.getUser().getName())
                .title(board.getTitle())
                .content(board.getContent())
                .categoryName(board.getCategory().getCategoryName())
                .notice(board.isNotice())
                .commentCount(board.getCommentCount())
                .viewCount(board.getViewCount())
                .likeCount(board.getLikeCount())
                .createdAt(board.getCreatedAt())
                .imageList(ImageResponse.toResponse(board))
                .commentList(CommentResponse.toCommentResponseList(board.getComments()))
                .existImage(!images.isEmpty())
                .build();


    }


}
