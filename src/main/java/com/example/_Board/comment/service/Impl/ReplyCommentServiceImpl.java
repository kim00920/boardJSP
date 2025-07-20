package com.example._Board.comment.service.Impl;

import com.example._Board.board.domain.Board;
import com.example._Board.board.repository.BoardRepository;
import com.example._Board.comment.controller.request.ReplyCommentCreateRequest;
import com.example._Board.comment.controller.request.ReplyCommentEditRequest;
import com.example._Board.comment.domain.Comment;
import com.example._Board.comment.domain.ReplyComment;
import com.example._Board.comment.repository.CommentRepository;
import com.example._Board.comment.repository.ReplyCommentRepository;
import com.example._Board.comment.service.ReplyCommentService;
import com.example._Board.error.BusinessException;
import com.example._Board.user.domain.User;
import com.example._Board.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example._Board.error.ErrorCode.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ReplyCommentServiceImpl implements ReplyCommentService {

    private final UserRepository userRepository;
    private final ReplyCommentRepository replyCommentRepository;
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    // 대댓글 생성
    @Override
    public void ReplyCommentCreate(Long commentId, ReplyCommentCreateRequest request) {
        User user = getUser();

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException(NOT_FOUND_COMMENT));

        ReplyComment replyComment = ReplyComment.createReplyComment(user, comment, request);

        replyCommentRepository.save(replyComment);

        Board board = comment.getBoard();

        long replyCommentCount = commentRepository.replyCommentCount(board.getId());
        long commentCount = commentRepository.commentCount(board.getId());
        long totalCount = commentCount + replyCommentCount;

        boardRepository.updateCommentCount(board.getId(), totalCount);
    }


    // 대댓글 수정
    @Override
    public void ReplyCommentEdit(Long replyCommentId, ReplyCommentEditRequest request) {
        User user = getUser();
        ReplyComment replyComment = ExistUserWriterCheck(replyCommentId, user);
        replyComment.editReplyComment(request.getReplyComment());
    }


    // 대댓글 삭제
    @Override
    public void ReplyCommentDelete(Long replyCommentId) {
        User user = getUser();
        ReplyComment replyComment = ExistUserWriterCheck(replyCommentId, user);
        replyCommentRepository.delete(replyComment);
    }

    @Override
    public Long findBoardIdByCommentId(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException(NOT_FOUND_COMMENT));
        return comment.getBoard().getId();
    }

    // 회원 객체 반환
    private User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginId = authentication.getName();

        log.info("로그인 회원 {}", loginId);

        return userRepository.findByLoginId(loginId).orElseThrow(
                () -> new BusinessException(NOT_FOUND_USER));
    }

    //  대댓글 작성자 체크
    private ReplyComment ExistUserWriterCheck(Long replyCommentId, User user) {
        return replyCommentRepository.findByIdAndUserId(replyCommentId, user.getId()).orElseThrow(
                () -> new BusinessException(NOT_MATCH_COMMENT));
    }
}
