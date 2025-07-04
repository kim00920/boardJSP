package com.example.boardtest.board.service;

import com.example._Board.board.controller.request.BoardCreateRequest;
import com.example._Board.board.controller.request.BoardEditRequest;
import com.example._Board.board.controller.response.BoardPageResponse;
import com.example._Board.board.controller.response.BoardResponse;
import com.example._Board.board.domain.Board;
import com.example._Board.board.domain.BoardSortType;
import com.example._Board.board.domain.Category;
import com.example._Board.board.domain.Image;
import com.example._Board.board.repository.BoardRepository;
import com.example._Board.board.repository.CategoryRepository;
import com.example._Board.board.repository.ImageRepository;
import com.example._Board.board.service.Impl.BoardServiceImpl;
import com.example._Board.config.s3.S3Service;
import com.example._Board.user.domain.User;
import com.example._Board.user.repository.UserRepository;
import com.example.boardtest.factory.BoardFactory;
import com.example.boardtest.factory.CategoryFactory;
import com.example.boardtest.factory.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
@DisplayName("게시글 테스트")
public class BoardServiceTest {

    @InjectMocks
    BoardServiceImpl boardService;

    @Mock
    BoardRepository boardRepository;

    @Mock
    ImageRepository imageRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    CategoryRepository categoryRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    S3Service s3Service;

    @BeforeEach()
    void beforeEach() {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        TestingAuthenticationToken mockAuthentication = new TestingAuthenticationToken("loginId", "1234");
        context.setAuthentication(mockAuthentication);
        SecurityContextHolder.setContext(context);
    }

    @Test
    @DisplayName("게시글 등록 (이미지 2개 + 카테고리)")
    void boardCreate() {
        UserFactory userFactory = new UserFactory(passwordEncoder);
        Board board = BoardFactory.createBoard(userFactory);
        Category category = board.getCategory();

        BoardCreateRequest boardCreateRequest = BoardCreateRequest.builder()
                .title(board.getTitle())
                .content(board.getContent())
                .categoryId(category.getId())
                .build();

        MultipartFile mockFile1 = mock(MultipartFile.class);
        MultipartFile mockFile2 = mock(MultipartFile.class);
        List<MultipartFile> mockImgPaths = List.of(mockFile1, mockFile2);

        List<String> fakeUrls = List.of("https://fake/image1.png", "https://fake/image2.png");
        given(s3Service.upload(any())).willReturn(fakeUrls);


        given(userRepository.findByLoginId(any())).willReturn(Optional.of(board.getUser()));
        given(categoryRepository.findById(category.getId())).willReturn(Optional.of(category));

        boardService.boardCreate(boardCreateRequest, mockImgPaths);

        verify(boardRepository).save(any());
        verify(imageRepository).saveAll(any());
    }

    @Test
    @DisplayName("게시글 수정")
    void boardEdit() {
        UserFactory userFactory = new UserFactory(passwordEncoder);
        Board board = BoardFactory.createBoard(userFactory);
        Category category = board.getCategory();

        BoardEditRequest boardEditRequest = BoardEditRequest.builder()
                .title("제목 수정")
                .content("내용 수정")
                .categoryId(category.getId())
                .build();

        // 이미지 업로드 x
        List<MultipartFile> mockImgPaths = new ArrayList<>();

        given(userRepository.findByLoginId(any())).willReturn(Optional.of(board.getUser()));
        given(boardRepository.findById(board.getId())).willReturn(Optional.of(board));
        given(categoryRepository.findById(category.getId())).willReturn(Optional.of(category));

        // 기존 게시글에 이미지 1개 있다고 가정
        Image existingImage = new Image("https://fake/image1.png", board); // 기존 이미지 객체 생성 (필요한 속성 설정)
        existingImage.setBoard(board);
        given(imageRepository.findByBoardId(board.getId())).willReturn(List.of(existingImage));


        boardService.boardEdit(board.getId(), boardEditRequest, mockImgPaths);

        verify(imageRepository).deleteById(existingImage.getId());  // 기존 이미지 삭제 검증
        verify(s3Service).deleteFile(existingImage.getFileUrl());  // S3에서 파일 삭제 검증
    }

    @Test
    @DisplayName("게시글 삭제 (이미지도 같이 삭제)")
    void boardDelete() {
        UserFactory userFactory = new UserFactory(passwordEncoder);
        Board board = BoardFactory.createBoard(userFactory);
        User user = board.getUser();

        Image image = new Image(1L, "https://fake/image1.png", board);
        image.setBoard(board);

        given(userRepository.findByLoginId(any())).willReturn(Optional.of(user));
        given(boardRepository.findById(board.getId())).willReturn(Optional.of(board));
        given(imageRepository.findByBoardId(board.getId())).willReturn(List.of(image));

        boardService.boardDelete(board.getId());

        verify(imageRepository).deleteById(image.getId());
        verify(s3Service).deleteFile(image.getFileUrl());
        verify(boardRepository).deleteById(board.getId());
    }

    @Test
    @DisplayName("특정 게시글 조회 (조회수 1 증가) ")
    void boardDetail() {
        UserFactory userFactory = new UserFactory(passwordEncoder);
        Board board = BoardFactory.createBoard(userFactory);

        given(boardRepository.findById(anyLong())).willReturn(Optional.of(board));

        BoardResponse boardResponse = boardService.boardFindById(board.getId());

        assertThat(boardResponse.getCategoryName()).isEqualTo("일상");
        assertThat(boardResponse.getTitle()).isEqualTo("제목1");

        // 조회수는 1 증가 해야됨
        assertThat(boardResponse.getViewCount()).isEqualTo(101);
    }

    @DisplayName("정렬 조건에 따른 게시글 페이징 조회")
    @Test
    void boardsFindBySort() {
        UserFactory userFactory = new UserFactory(passwordEncoder);
        User user = userFactory.createUser1();

        Board board1 = Board.builder()
                .id(1L)
                .title("조회수가 100인 게시글")
                .content("내용")
                .viewCount(100)
                .likeCount(50)
                .category(new Category(1L, "일상"))
                .user(user)
            .build();

        Board board2 = Board.builder()
                .id(2L)
                .title("조회수가 50인 게시글")
                .content("내용")
                .viewCount(50)
                .likeCount(30)
                .category(new Category(1L, "일상"))
                .user(user)
            .build();

        List<BoardResponse> responseList = List.of(
                BoardResponse.toBoardResponse(board1),
                BoardResponse.toBoardResponse(board2));

        Page<BoardResponse> boardPage = new PageImpl<>(responseList);

        given(boardRepository.findBoardsBySort(eq(BoardSortType.VIEW_DESC), any(Pageable.class)))
                .willReturn(boardPage);


        Pageable pageable = PageRequest.of(0, 10);
        Page<BoardResponse> result = boardService.boardsFindBySort(BoardSortType.VIEW_DESC, pageable);


        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("조회수가 100인 게시글");
        assertThat(result.getContent().get(1).getTitle()).isEqualTo("조회수가 50인 게시글");

        verify(boardRepository).findBoardsBySort(BoardSortType.VIEW_DESC, pageable);

    }

    @DisplayName("게시글 검색 기능")
    @Test
    void boardFindKeyword() {

        UserFactory userFactory = new UserFactory(passwordEncoder);
        Board board = BoardFactory.createBoard(userFactory);
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "id");
        List<Board> list = new ArrayList<>();
        list.add(board);
        PageImpl<Board> page = new PageImpl<>(list);
        String keyword = "제목";


        given(boardRepository.findByIsNoticeFalseAndTitleContaining(keyword, pageable)).willReturn(page);

        Page<BoardResponse> responses = boardService.boardFindKeyword(keyword, pageable);

        assertThat(responses.getContent().get(0).getTitle()).isEqualTo("제목1");
    }
}




