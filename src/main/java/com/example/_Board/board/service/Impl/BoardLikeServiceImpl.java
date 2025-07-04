package com.example._Board.board.service.Impl;

import com.example._Board.board.controller.response.BoardLikeResponse;
import com.example._Board.board.controller.response.BoardResponse;
import com.example._Board.board.domain.Board;
import com.example._Board.board.domain.BoardLike;
import com.example._Board.board.repository.BoardLikeRepository;
import com.example._Board.board.repository.BoardRepository;
import com.example._Board.board.service.BoardLikeService;
import com.example._Board.error.BusinessException;
import com.example._Board.user.domain.User;
import com.example._Board.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class BoardLikeServiceImpl implements BoardLikeService {

    private final BoardRepository boardRepository;
    private final BoardLikeRepository boardLikeRepository;
    private final UserRepository userRepository;


    // 좋아요 누르기
    @Override
    public void increaseLike(Long boardId) {
        User user = getUser();

        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new BusinessException(NOT_FOUND_BOARD));

        if (hasUserLikedBoard(board, user)) {
            throw new BusinessException(DUPLICATED_LIKE_BOARD);
        }

        BoardLike boardLike = BoardLike.increaseBoardLike(board, user);
        board.increaseLikeCount();

        boardLikeRepository.save(boardLike);
    }

    // 좋아요 취소
    @Override
    public void decreaseLike(Long boardId) {
        User user = getUser();
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new BusinessException(NOT_FOUND_BOARD));

        if (!hasUserLikedBoard(board, user)) {
            throw new BusinessException(NOT_LIKE_BOARD);
        }

        BoardLike boardLike = boardLikeRepository.findByBoardAndUser(board, user).orElseThrow(
                () -> new BusinessException(NOT_LIKE_BOARD));

        boardLikeRepository.delete(boardLike);
        board.decreaseLikeCount();
    }

    // 좋아요 전체 조회
    @Override
    @Transactional(readOnly = true)
    public List<BoardLikeResponse> findAllByBoardId(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new BusinessException(NOT_FOUND_BOARD));

        List<BoardLike> boardLikes = boardLikeRepository.findAllByBoard(board);

        return boardLikes.stream()
                .map(BoardLikeResponse::new)
                .toList();
    }

    @Override
    public boolean isLiked(Long boardId) {
        User user = getUser();
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new BusinessException(NOT_FOUND_BOARD));


        return hasUserLikedBoard(board, user);
    }


    // 회원 객체 반환
    private User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginId = authentication.getName();

        log.info("로그인 회원 {}", loginId);

        return userRepository.findByLoginId(loginId).orElseThrow(
                () -> new BusinessException(NOT_FOUND_USER));
    }

    // 회원이 좋아요를 눌렀는지?
    private boolean hasUserLikedBoard(Board board, User user) {
        return boardLikeRepository.existsByBoardAndUser(board, user);
    }

}
