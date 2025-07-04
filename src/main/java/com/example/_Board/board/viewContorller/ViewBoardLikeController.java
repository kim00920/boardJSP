package com.example._Board.board.viewContorller;

import com.example._Board.board.controller.response.BoardLikeResponse;
import com.example._Board.board.service.BoardLikeService;
import com.example._Board.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board/{boardId}/like")
public class ViewBoardLikeController {


    private final BoardLikeService boardLikeService;

    // 좋아요 누르기
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void increaseLike(@PathVariable("boardId") Long boardId) {
        boardLikeService.increaseLike(boardId);
    }

    // 좋아요 취소
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void decreaseLike(@PathVariable("boardId") Long boardId) {
        boardLikeService.decreaseLike(boardId);
    }

    // 좋아요 눌렀는지 확인
    @GetMapping("/status")
    public Map<String, Boolean> likeStatus(@PathVariable Long boardId) {
        boolean liked = boardLikeService.isLiked(boardId);
        return Collections.singletonMap("liked", liked);
    }

    // 좋아요 누른 회원 목록
    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    public List<BoardLikeResponse> findAllBoardLike(@PathVariable("boardId") Long boardId) {
        return boardLikeService.findAllByBoardId(boardId);
    }

}
