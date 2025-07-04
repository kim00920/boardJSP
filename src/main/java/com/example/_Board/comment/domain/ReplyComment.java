package com.example._Board.comment.domain;


import com.example._Board.comment.controller.request.ReplyCommentCreateRequest;
import com.example._Board.config.BaseTimeEntity;
import com.example._Board.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "replyComment")
@Entity
public class ReplyComment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "replyComment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;   //  댓글

    @Column(nullable = false)
    private String replyComment;  // 대댓글 내용

    @Builder
    public ReplyComment(Long id, User user, Comment comment, String replyComment) {
        this.id = id;
        this.user = user;
        this.comment = comment;
        this.replyComment = replyComment;
    }

    // 대댓글 수정
    public void editReplyComment(String replyComment) {
        this.replyComment = replyComment;
    }

    // 대댓글 생성
    public static ReplyComment createReplyComment(User user, Comment comment, ReplyCommentCreateRequest replyCommentCreateRequest) {
        return ReplyComment.builder()
                .user(user)
                .comment(comment)
                .replyComment(replyCommentCreateRequest.getReplyComment())
                .build();
    }
}
