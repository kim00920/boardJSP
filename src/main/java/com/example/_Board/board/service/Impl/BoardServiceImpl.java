package com.example._Board.board.service.Impl;

import com.example._Board.board.controller.request.BoardCreateRequest;
import com.example._Board.board.controller.request.BoardEditRequest;
import com.example._Board.board.controller.response.BoardPageResponse;
import com.example._Board.board.controller.response.BoardResponse;
import com.example._Board.board.domain.Board;
import com.example._Board.board.domain.Category;
import com.example._Board.board.domain.Image;
import com.example._Board.board.repository.BoardRepository;
import com.example._Board.board.repository.CategoryRepository;
import com.example._Board.board.repository.ImageRepository;
import com.example._Board.board.service.BoardService;
import com.example._Board.board.domain.BoardSortType;
import com.example._Board.config.s3.S3Service;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

import static com.example._Board.error.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BoardServiceImpl implements BoardService {

    private final S3Service s3Service;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;

    // 게시글 생성 + S3 이미지(NULL 가능) + 카테고리
    @Override
    public void boardCreate(BoardCreateRequest boardCreateRequest, List<MultipartFile> imgPaths) {
        User user = getUser();

        Category category = categoryRepository.findById(boardCreateRequest.getCategoryId()).orElseThrow(
                () -> new BusinessException(NOT_FOUND_CATEGORY));

        Board board = Board.BoardCreate(boardCreateRequest, category, user);
        boardRepository.save(board);

        if (imgPaths != null && !imgPaths.isEmpty()) {
            List<String> list = s3Service.upload(imgPaths);

            List<Image> imageList = list.stream()
                    .map(img -> Image.builder()
                            .fileUrl(img)
                            .build())
                            .toList();

            for (Image image : imageList) {
                image.setBoard(board);
            }

            imageRepository.saveAll(imageList); // 1개
        }
    }

    // 게시글 수정 + S3 이미지(NULL 가능) + 카테고리
    @Override
    public void boardEdit(Long boardId, BoardEditRequest boardEditRequest, List<MultipartFile> imgPaths) {
        User user = getUser();

        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new BusinessException(NOT_FOUND_BOARD));

        if (!user.getId().equals(board.getUser().getId())) { // 같은 작성자만 가능
            throw new BusinessException(NOT_FOUND_USER);
        }

        Category category = categoryRepository.findById(boardEditRequest.getCategoryId()).orElseThrow(
                () -> new BusinessException(NOT_FOUND_CATEGORY));

        board.BoardEdit(boardEditRequest, category);

        List<Image> imageList = imageRepository.findByBoardId(board.getId());
        for (Image image : imageList) {
            String fileName = image.getFileUrl();
            s3Service.deleteFile(fileName);
            imageRepository.deleteById(image.getId());
        }

        if (imgPaths != null && !imgPaths.isEmpty()) {
            List<String> list = s3Service.upload(imgPaths);

            // 이미지 정보 저장
            List<Image> images = list.stream()
                    .map(img -> Image.builder()
                            .fileUrl(img)
                            .board(board)
                            .build())
                    .collect(Collectors.toList());

            for (Image image : images) {
                image.setBoard(board);
            }
            imageRepository.saveAll(images);
        }
    }

    // 게시글 삭제
    @Override
    public void boardDelete(Long boardId) {
        User user = getUser();

        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new BusinessException(NOT_FOUND_BOARD));

        if (!user.getId().equals(board.getUser().getId())) { // 같은 작성자만 가능
            throw new BusinessException(NOT_FOUND_USER);
        }

        List<Image> imageList = imageRepository.findByBoardId(board.getId());

        for (Image image : imageList) {
            String fileName = image.getFileUrl();
            s3Service.deleteFile(fileName);
            imageRepository.deleteById(image.getId());
        }
        boardRepository.deleteById(board.getId());
    }

    // 공지글로 등록
    @Override
    public void boardSetNotice(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new BusinessException(NOT_FOUND_BOARD));

        if (board.isNotice()) {
            board.setIsNotice(false);
        } else {
            board.setIsNotice(true);

        }
        boardRepository.save(board);
    }

    // 게시글 전체 조회
    @Override
    @Transactional(readOnly = true)
    public Page<BoardPageResponse> boardsFindAll(Pageable pageable) {

        return boardRepository.findBoardsWithNoticeFirst(pageable);
    }

    // 조회수, 좋아요가 높은거나 낮은걸로 정렬 조회
    @Override
    @Transactional(readOnly = true)
    public Page<BoardResponse> boardsFindBySort(BoardSortType sortType, Pageable pageable) {
        return boardRepository.findBoardsBySort(sortType, pageable);
    }

    // 게시글 단건 조회
    @Override
    public BoardResponse boardFindById(Long boardId) {
        Board board = boardRepository.findByIdWithCommentsAndUsers(boardId).orElseThrow(
                () -> new BusinessException(NOT_FOUND_BOARD));

//        Board board = boardRepository.findByIdWithAll(boardId).orElseThrow(
//                () -> new BusinessException(NOT_FOUND_BOARD));

        board.increaseViewCount();


        return BoardResponse.toBoardResponse(board);
    }


    // 게시글 검색 기능
    @Override
    @Transactional(readOnly = true)
    public Page<BoardResponse> boardFindKeyword(String keyword, Pageable pageable) {
        Page<Board> board = boardRepository.findByIsNoticeFalseAndTitleContaining(keyword, pageable);

        return board.map(BoardResponse::new);
    }



    @Override
    public Long getLikeCnt(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BusinessException(NOT_FOUND_BOARD));

        return (long) board.getLikeCount();
    }

    // 현재 게시글 공지글 상태
    @Override
    public boolean isNotice(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BusinessException(NOT_FOUND_BOARD));

        return board.isNotice();
    }


    // 현재 로그인 중인 회원의 정보를 가져옴
    private User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByLoginId(authentication.getName()).orElseThrow(() -> new BusinessException(NOT_FOUND_USER));
    }
}
