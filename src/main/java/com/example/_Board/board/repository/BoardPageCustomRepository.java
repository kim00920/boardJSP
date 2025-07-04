package com.example._Board.board.repository;

import com.example._Board.board.controller.response.BoardPageResponse;
import com.example._Board.board.controller.response.BoardResponse;
import com.example._Board.board.domain.BoardSortType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardPageCustomRepository {

    // 정렬탭 같은 부분, 조회수가 많은 순, 적은 순, 좋아요가 많은 순, 적은 순 으로 정렬 조회
    Page<BoardResponse> findBoardsBySort(BoardSortType sortType, Pageable pageable);

    // 게시글 메인화면 default 값은 공지글 + 일반글 상태로 게시글을 조회함
    Page<BoardPageResponse> findBoardsWithNoticeFirst(Pageable pageable);

}
