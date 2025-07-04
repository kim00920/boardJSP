package com.example.boardtest.board.service;

import com.example._Board.board.domain.Board;
import com.example._Board.board.domain.BoardLike;
import com.example._Board.board.repository.BoardLikeRepository;
import com.example._Board.board.repository.BoardRepository;
import com.example._Board.board.repository.CategoryRepository;
import com.example._Board.board.repository.ImageRepository;
import com.example._Board.board.service.BoardLikeService;
import com.example._Board.board.service.Impl.BoardLikeServiceImpl;
import com.example._Board.board.service.Impl.BoardServiceImpl;
import com.example._Board.config.s3.S3Service;
import com.example._Board.user.domain.User;
import com.example._Board.user.repository.UserRepository;
import com.example.boardtest.factory.BoardFactory;
import com.example.boardtest.factory.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("좋아요 테스트")
public class BoardLikeServiceTest {

    @InjectMocks
    BoardLikeServiceImpl boardLikeService;

    @Mock
    BoardRepository boardRepository;

    @Mock
    BoardLikeRepository boardLikeRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;


    @BeforeEach()
    void beforeEach() {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        TestingAuthenticationToken mockAuthentication = new TestingAuthenticationToken("loginId", "1234");
        context.setAuthentication(mockAuthentication);
        SecurityContextHolder.setContext(context);
    }

    @Test
    @DisplayName("좋아요 누르기 (좋아요 1 증가)")
    void increaseLike() {
        UserFactory userFactory = new UserFactory(passwordEncoder);
        User user = userFactory.createUser1();
        Board board = BoardFactory.createBoard(userFactory);

        given(userRepository.findByLoginId(any())).willReturn(Optional.of(user));
        given(boardRepository.findById(any())).willReturn(Optional.of(board));
        given(boardLikeRepository.existsByBoardAndUser(any(), any())).willReturn(false);

        boardLikeService.increaseLike(board.getId());

        assertThat(board.getLikeCount()).isEqualTo(11);

        verify(boardLikeRepository).save(any(BoardLike.class));
    }

    @Test
    @DisplayName("좋아요 취소 (좋아요 1 감소)")
    void decreaseLike_success() {
        UserFactory userFactory = new UserFactory(passwordEncoder);
        User user = userFactory.createUser1();
        Board board = BoardFactory.createBoard(userFactory);
        BoardLike boardLike = BoardLike.increaseBoardLike(board, user);

        given(userRepository.findByLoginId(any())).willReturn(Optional.of(user));
        given(boardRepository.findById(any())).willReturn(Optional.of(board));
        given(boardLikeRepository.existsByBoardAndUser(any(), any())).willReturn(true);
        given(boardLikeRepository.findByBoardAndUser(any(), any())).willReturn(Optional.of(boardLike));

        boardLikeService.decreaseLike(board.getId());

        assertThat(board.getLikeCount()).isEqualTo(9); // 원래 10에서 -1

        verify(boardLikeRepository).delete(any(BoardLike.class));
    }

    @Test
    @DisplayName("이미 좋아요를 누른 상태면 예외 발생")
    void increaseLike_duplicateLike_throwsException() {
        UserFactory userFactory = new UserFactory(passwordEncoder);
        User user = userFactory.createUser1();
        Board board = BoardFactory.createBoard(userFactory);

        given(userRepository.findByLoginId(any())).willReturn(Optional.of(user));
        given(boardRepository.findById(any())).willReturn(Optional.of(board));
        given(boardLikeRepository.existsByBoardAndUser(any(), any())).willReturn(true); // 이미 좋아요 누른 상태

        assertThatThrownBy(() -> boardLikeService.increaseLike(board.getId()))
                .isInstanceOf(com.example._Board.error.BusinessException.class)
                .hasMessage( "이미 좋아요를 눌렀습니다."); // 혹은 ErrorCode에 설정된 메시지

        verify(boardLikeRepository, never()).save(any(BoardLike.class));
    }

}
