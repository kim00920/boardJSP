package com.example._Board.board.service;

import com.example._Board.board.controller.request.BoardCreateRequest;
import com.example._Board.board.controller.request.BoardEditRequest;
import com.example._Board.board.controller.response.BoardPageResponse;
import com.example._Board.board.controller.response.BoardResponse;
import com.example._Board.board.domain.BoardSortType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BoardService {

    // 게시글 생성 (카테고리 + S3 이미지)
    void boardCreate(BoardCreateRequest boardCreateRequest, List<MultipartFile> imgPaths);

    // 게시글 수정 (카테고리 + S3 이미지)
    void boardEdit(Long boardId, BoardEditRequest boardEditRequest, List<MultipartFile> imgPaths);

    // 게시글 삭제
    void boardDelete(Long boardId);

    // 공지글로 등록
    void boardSetNotice(Long boardId);

    // 게시글 전체 조회 (일반 게시글 + 공지 게시글) + 페이징 처리
    Page<BoardPageResponse> boardsFindAll(Pageable pageable);

    // 게시글 정렬 조회 (좋아요, 조회수)
    Page<BoardResponse> boardsFindBySort(BoardSortType sortType, Pageable pageable);

    // 게시글 한 개 조회 (조회 할 때 마다 조회수 1 증가)
    BoardResponse boardFindById(Long boardId);

    // 게시글 검색 기능
    Page<BoardResponse> boardFindKeyword(String keyword, Pageable pageable);

    Long getLikeCnt(Long boardId);

    boolean isNotice(Long boardId);
}
