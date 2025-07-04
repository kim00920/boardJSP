package com.example._Board.comment.service;

import com.example._Board.comment.controller.request.ReplyCommentCreateRequest;
import com.example._Board.comment.controller.request.ReplyCommentEditRequest;

public interface ReplyCommentService {

    // 대댓글 생성
    void ReplyCommentCreate(Long commentId, ReplyCommentCreateRequest request);

    // 대댓글 수정
    void ReplyCommentEdit(Long replyCommentId, ReplyCommentEditRequest request);

    // 대댓글 삭제
    void ReplyCommentDelete(Long replyCommentId);

    // 이거 댓글로 옮겨야ㅐ됨
    Long findBoardIdByCommentId(Long commentId);
}
