package com.example._Board.comment.service.Impl;

import com.example._Board.board.domain.Board;
import com.example._Board.board.domain.Category;
import com.example._Board.board.repository.BoardRepository;
import com.example._Board.comment.controller.request.CommentCreateRequest;
import com.example._Board.comment.controller.request.CommentEditRequest;
import com.example._Board.comment.controller.response.CommentResponse;
import com.example._Board.comment.domain.Comment;
import com.example._Board.comment.repository.CommentRepository;
import com.example._Board.comment.service.CommentService;
import com.example._Board.error.BusinessException;
import com.example._Board.user.domain.User;
import com.example._Board.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example._Board.error.ErrorCode.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    // 댓글 생성
    @Override
    public void commentCreate(Long boardId, CommentCreateRequest commentCreateRequest) {
        log.info("댓글 생성 시작 ");

        User user = getUser();

        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new BusinessException(NOT_FOUND_BOARD));

        Comment comment = Comment.createComment(user, board, commentCreateRequest);

        commentRepository.save(comment);
        commentRepository.flush();

        // board.getComments() 호출해서 프록시 클래스명 출력
        Category category = board.getCategory();
        List<Comment> commentsProxy = board.getComments();
        log.info("board.getComments() 프록시 클래스: {}", commentsProxy.getClass().getName());
        log.info(" board.getCategory()) 프록시 클래스: {}", board.getCategory().getClass().getName());




        // 실제 데이터 접근 시점: 댓글 개수 조회 (이 시점에 쿼리 발생)
        // log.info("댓글 개수 조회: {}", commentsProxy.size());
        long replyCommentCount = commentRepository.replyCommentCount(boardId); // 대댓글
        long commentCount = commentRepository.commentCount(boardId); // 댓글

        long totalCount = commentCount + replyCommentCount; // 총 댓글 개수

        boardRepository.updateCommentCount(boardId, totalCount);

        log.info("현재 댓글 개수: {}", totalCount);

        log.info("댓글 생성 완료 ");
    }

    // 특정게시글의 댓글 전체 조회 (대댓글 포함되어있음) + 페이징
    @Override
    public Page<CommentResponse> commentFindAllByBoard(Long boardId, Pageable pageable) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new BusinessException(NOT_FOUND_BOARD));

        Page<Comment> commentPage = commentRepository.findAllByBoardWithUser(board, pageable);

        //  Page<Comment> commentPage = commentRepository.findAllByBoard(board, pageable);
        return commentPage.map(CommentResponse::new);
    }

    // 댓글 전체 조회
    @Override
    @Transactional(readOnly = true)
    public List<CommentResponse> commentFindAll() {
        List<Comment> commentList = commentRepository.findAll();
        return commentList.stream()
                .map(CommentResponse::new)
                .toList();
    }

    // 댓글 단건 조회 (대댓글 포함되어있음)
    @Override
    @Transactional(readOnly = true)
    public CommentResponse commentFindOne(Long id) {
        Comment comment  = commentRepository.findById(id).orElseThrow(() -> new BusinessException(NOT_FOUND_COMMENT));

        return CommentResponse.toDto(comment);
    }

    // 댓글 수정
    @Override
    public void commentEdit(Long commentId, CommentEditRequest commentEditRequest) {
        User user = getUser();
        Comment comment = ExistCommentCheck(commentId);
        WriterCommentUserEqLoginUser(user, comment);
        comment.editComment(commentEditRequest);
    }


    // 댓글 삭제
    @Override
    public void commentDelete(Long id) {
        User user = getUser();
        Comment comment = ExistCommentCheck(id);
        WriterCommentUserEqLoginUser(user, comment);
        commentRepository.delete(comment);
    }

    // 회원 객체 반환
    private User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginId = authentication.getName();

        log.info("로그인 회원 {}", loginId);

        return userRepository.findByLoginId(loginId).orElseThrow(
                () -> new BusinessException(NOT_FOUND_USER));
    }

    // 댓글 작성한 회원 일치여부
    private void WriterCommentUserEqLoginUser(User user, Comment comment) {
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new BusinessException(NOT_MATCH_COMMENT);
        }
    }

    // 댓글이 존재하는지?
    private Comment ExistCommentCheck(Long commentId) {

        return commentRepository.findById(commentId).orElseThrow(
                () -> new BusinessException(NOT_FOUND_COMMENT));
    }
}
