package com.example._Board.board.viewContorller;

import com.example._Board.board.controller.request.BoardCreateRequest;
import com.example._Board.board.controller.request.BoardEditRequest;
import com.example._Board.board.controller.response.BoardPageResponse;
import com.example._Board.board.controller.response.BoardResponse;
import com.example._Board.board.controller.response.CategoryResponse;
import com.example._Board.board.domain.Board;
import com.example._Board.board.domain.BoardSortType;
import com.example._Board.board.repository.CategoryRepository;
import com.example._Board.board.service.BoardService;
import com.example._Board.board.service.CategoryService;
import com.example._Board.error.BusinessException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ViewBoardController {

    private final BoardService boardService;
    private final CategoryService categoryService;


    // 게시글 처음화면
    @GetMapping("/main")
    public String boardsFindAll(
            @PageableDefault(size = 8) Pageable pageable,
            Model model) {

        Page<BoardPageResponse> boardsPage = boardService.boardsFindAll(pageable);
        model.addAttribute("boards", boardsPage);

        return "main";
    }

    // 게시글 정렬 조회 >> 조회수 또는 좋아요
    @GetMapping("/sorted/{sortType}")
    public String sortedBoardsPage(@PathVariable("sortType") BoardSortType sortType,
                                   @PageableDefault(size = 8) Pageable pageable,
                                   Model model) {
        Page<BoardResponse> boards = boardService.boardsFindBySort(sortType, pageable);

        boards.getContent().forEach(board -> {
            String formatted = board.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            board.setFormattedCreatedAt(formatted);
        });

        model.addAttribute("boards", boards);
        model.addAttribute("currentSortType", sortType); // 정렬 상태 유지용
        return "main";
    }

    // 게시글 제목을 파라미터로 받아서 검색
    @GetMapping("/search")
    public String boardSearch(
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC, size = 8) Pageable pageable,
            Model model) {

        Page<BoardResponse> boards = boardService.boardFindKeyword(keyword, pageable);

        boards.getContent().forEach(board -> {
            if (board.getCreatedAt() != null) {
                board.setFormattedCreatedAt(board.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            }
        });

        model.addAttribute("boards", boards);
        model.addAttribute("keyword", keyword);

        return "main";
    }

    // 특정 게시글 조회
    @GetMapping("/board/{boardId}")
    public String boardFindOne(@PathVariable("boardId") Long boardId, Model model) {
        BoardResponse boardResponse = boardService.boardFindById(boardId);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String createdAtStr = boardResponse.getCreatedAt().format(formatter);

        model.addAttribute("board", boardResponse);
        model.addAttribute("createdAtDate", createdAtStr);

        return "boardInfo";
    }


    // 게시글 작성 페이지로 이동
    @GetMapping("/boardForm")
    public String boardFormPage(Model model) {
        List<CategoryResponse> categories = categoryService.categoryFindAll();
        model.addAttribute("categories", categories);
        return "boardForm";
    }

    // 게시글 작성 하기
    @PostMapping("/boardForm")
    public String createBoard(
            @Valid BoardCreateRequest boardCreateRequest,
            @RequestParam(value = "multipartFile", required = false) List<MultipartFile> multipartFiles) {

        boardService.boardCreate(boardCreateRequest, multipartFiles);

        return "redirect:/main";
    }


    // 게시글 수정 폼 이동
    @GetMapping("/board/{boardId}/updateForm")
    public String boardUpdateFormPage(@PathVariable("boardId") Long boardId, Model model) {
        BoardResponse board = boardService.boardFindById(boardId);

        List<CategoryResponse> categories = categoryService.categoryFindAll();

        System.out.println(categories);
        model.addAttribute("board", board);
        model.addAttribute("categories", categories);


        return "boardUpdateForm";
    }

    // 게시글 수정 하기
    @PostMapping("/board/{boardId}")
    public String boardEdit(@PathVariable("boardId") Long boardId,
                            @Valid BoardEditRequest request,
                            @RequestParam(required = false) List<MultipartFile> multipartFile) {
        boardService.boardEdit(boardId, request, multipartFile);
        return "redirect:/board/" + boardId;
    }


    // 게시글 삭제 하기
    @DeleteMapping("/board/{boardId}")
    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void boardDelete(@PathVariable("boardId") Long boardId) {

        boardService.boardDelete(boardId);
    }

    // 좋아요 수 비동기로 보내기
    @GetMapping("/board/{boardId}/like/count")
    @ResponseBody
    public Map<String, Long> getLikeCount(@PathVariable("boardId") Long boardId) {
        Long boardLikeCnt = boardService.getLikeCnt(boardId);

        return Collections.singletonMap("count", boardLikeCnt);
    }

    // 게시글 공지글 지정
    @PutMapping("/board/{boardId}/setNotice")
    @ResponseStatus(HttpStatus.OK)
    public void boardSetNotice(@PathVariable("boardId") Long boardId) {
        boardService.boardSetNotice(boardId);
    }


    // 현재 게시글 공지글 상태 가져오기
    @GetMapping("/board/{boardId}/notice/status")
    public Map<String, Boolean> noticeStatus(@PathVariable("boardId") Long boardId) {
        return Collections.singletonMap("notice", boardService.isNotice(boardId));
    }

}
