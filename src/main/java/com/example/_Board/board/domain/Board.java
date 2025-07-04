package com.example._Board.board.domain;

import com.example._Board.board.controller.request.BoardCreateRequest;
import com.example._Board.board.controller.request.BoardEditRequest;
import com.example._Board.board.controller.response.BoardResponse;
import com.example._Board.comment.domain.Comment;
import com.example._Board.config.BaseTimeEntity;
import com.example._Board.user.domain.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "boards", indexes = {
        @Index(name = "idx_is_notice_view_count", columnList = "is_notice, view_count"),
        @Index(name = "idx_is_notice_like_count", columnList = "is_notice, like_count")
})
public class Board extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column
    @Builder.Default
    private int viewCount = 0;

    @Column
    @Builder.Default
    private int likeCount = 0;

    @Column
    @Builder.Default
    private boolean isNotice = false; // 공지글인지? 기본값 0

    @Column(nullable = false)
    @Builder.Default
    private int commentCount = 0;

//    @Version
//    private int version; // 낙관 락 사용


    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @BatchSize(size = 5)
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @BatchSize(size = 100)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<BoardLike> likes = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category; // 카테고리

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;



    // 공지글로 설정
    public void setIsNotice(boolean isNotice) {
        this.isNotice = isNotice;
    }

    // 조회수 증가
    public void increaseViewCount() {
        this.viewCount++;
    }


    public Board(Long id, User user, String title, String content, int viewCount, int likeCount, boolean isNotice, Category category) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.isNotice = isNotice;
        this.category = category;
    }

    // 게시글 생성
    public static Board BoardCreate(BoardCreateRequest boardCreateRequest, Category category, User user) {
        return Board.builder()
                .user(user)
                .title(boardCreateRequest.getTitle())
                .content(boardCreateRequest.getContent())
                .category(category)
                .build();
    }

    // 게시글 수정
    public void BoardEdit(BoardEditRequest boardEditRequest, Category category) {
        this.title = boardEditRequest.getTitle();
        this.content = boardEditRequest.getContent();
        this.category = category;
    }

    // 좋아요 증가
    public void increaseLikeCount() {
        this.likeCount++;
    }

    // 좋아요 감소
    public void decreaseLikeCount() {
        this.likeCount--;
    }

    public boolean isNotice() {
        return this.isNotice;
    }

    public void setCommentCount(int count) {
        this.commentCount = count;
    }
}

