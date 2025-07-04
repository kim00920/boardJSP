package com.example._Board.board.controller;

import io.swagger.v3.oas.annotations.Operation;
import com.example._Board.board.controller.response.BoardLikeResponse;
import com.example._Board.board.service.BoardLikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/board/{boardId}/like")
public class BoardLikeController {

    private final BoardLikeService boardLikeService;

    @Operation(summary = "게시글 좋아요 누르기")
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void increaseLike(@PathVariable("boardId") Long boardId) {
        boardLikeService.increaseLike(boardId);
    }

    @Operation(summary = "게시글 좋아요 취소")
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void decreaseLike(@PathVariable("boardId") Long boardId) {
        boardLikeService.decreaseLike(boardId);
    }

    @Operation(summary = "특정 게시글 좋아요 조회")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BoardLikeResponse> findAllBoardLike(@PathVariable("boardId") Long boardId) {
        return boardLikeService.findAllByBoardId(boardId);
    }
}
