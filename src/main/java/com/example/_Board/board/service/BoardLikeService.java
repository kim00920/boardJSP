package com.example._Board.board.service;

import com.example._Board.board.controller.response.BoardLikeResponse;

import java.util.List;

public interface BoardLikeService {

    // 좋아요 누르기
    void increaseLike(Long boardId);

    // 좋아요 취소
    void decreaseLike(Long boardId);

    // 좋아요 전체 조회
    List<BoardLikeResponse> findAllByBoardId(Long boardId);

    boolean isLiked(Long boardId);
}
