package com.example.boardtest.comment.service;

import com.example._Board.board.domain.Board;
import com.example._Board.comment.controller.request.CommentEditRequest;
import com.example._Board.comment.controller.request.ReplyCommentCreateRequest;
import com.example._Board.comment.controller.request.ReplyCommentEditRequest;
import com.example._Board.comment.domain.Comment;
import com.example._Board.comment.domain.ReplyComment;
import com.example._Board.comment.repository.CommentRepository;
import com.example._Board.comment.repository.ReplyCommentRepository;
import com.example._Board.comment.service.Impl.ReplyCommentServiceImpl;
import com.example._Board.error.BusinessException;
import com.example._Board.user.domain.User;
import com.example._Board.user.repository.UserRepository;
import com.example.boardtest.factory.BoardFactory;
import com.example.boardtest.factory.CommentFactory;
import com.example.boardtest.factory.ReplyCommentFactory;
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
@DisplayName("대댓글 테스트")
public class ReplyCommentServiceTest {

    @InjectMocks
    ReplyCommentServiceImpl replyCommentService;

    @Mock
    UserRepository userRepository;

    @Mock
    ReplyCommentRepository replyCommentRepository;

    @Mock
    CommentRepository commentRepository;

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
    @DisplayName("대댓글 생성")
    void createReplyComment() {
        UserFactory userFactory = new UserFactory(passwordEncoder);
        User user = userFactory.createUser1();
        Board board = BoardFactory.createBoard(userFactory);
        Comment comment = ReplyCommentFactory.createComment(user, board);

        ReplyCommentCreateRequest request = ReplyCommentCreateRequest.builder()
                .replyComment("대댓글")
                .build();

        given(userRepository.findByLoginId(any())).willReturn(Optional.of(user));
        given(commentRepository.findById(any())).willReturn(Optional.of(comment));


        replyCommentService.ReplyCommentCreate(comment.getId(), request);


        verify(replyCommentRepository).save(any(ReplyComment.class));
    }

    @Test
    @DisplayName("대댓글 수정 성공 (작성자 O)")
    void editReplyCommentSuccess() {

        UserFactory userFactory = new UserFactory(passwordEncoder);
        User user = userFactory.createUser1();
        Board board = BoardFactory.createBoard(userFactory);
        Comment comment = ReplyCommentFactory.createComment(user, board);
        ReplyComment replyComment = ReplyCommentFactory.createReplyComment(user, comment);

        ReplyCommentEditRequest request = ReplyCommentEditRequest.builder()
                .replyComment("대댓글 수정")
                .build();

        given(userRepository.findByLoginId(user.getLoginId())).willReturn(Optional.of(user));
        given(replyCommentRepository.findByIdAndUserId(replyComment.getId(), user.getId())).willReturn(Optional.of(replyComment));


        replyCommentService.ReplyCommentEdit(replyComment.getId(), request);

        assertThat(replyComment.getReplyComment()).isEqualTo("대댓글 수정");
    }


    @Test
    @DisplayName("대댓글 삭제 성공 (작성자 O)")
    void deleteReplyCommentSuccess() {

        UserFactory userFactory = new UserFactory(passwordEncoder);
        User user = userFactory.createUser1();
        Board board = BoardFactory.createBoard(userFactory);
        Comment comment = ReplyCommentFactory.createComment(user, board);
        ReplyComment replyComment = ReplyCommentFactory.createReplyComment(user, comment);

        given(userRepository.findByLoginId(user.getLoginId())).willReturn(Optional.of(user));
        given(replyCommentRepository.findByIdAndUserId(replyComment.getId(), user.getId())).willReturn(Optional.of(replyComment));


        replyCommentService.ReplyCommentDelete(replyComment.getId());

        verify(replyCommentRepository).delete(replyComment);
    }

    @Test
    @DisplayName("대댓글 삭제 실패 (작성자 X)")
    void deleteReplyCommentFail() {
        // given
        UserFactory userFactory = new UserFactory(passwordEncoder);
        User user1 = userFactory.createUser1();
        User user2 = userFactory.createUser2();

        Board board = BoardFactory.createBoard(userFactory);
        Comment comment = ReplyCommentFactory.createComment(user1, board);
        ReplyComment replyComment = ReplyCommentFactory.createReplyComment(user1, comment);

        given(userRepository.findByLoginId(user2.getLoginId())).willReturn(Optional.of(user2));

        assertThatThrownBy(() -> replyCommentService.ReplyCommentDelete(replyComment.getId()))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("작성자만 가능합니다.");

        verify(replyCommentRepository, never()).delete(replyComment); // 삭제 호출 X
    }


    @Test
    @DisplayName("대댓글 수정 실패 (작성자 X)")
    void updateReplyCommentFail() {
        // given
        UserFactory userFactory = new UserFactory(passwordEncoder);
        User user1 = userFactory.createUser1();
        User user2 = userFactory.createUser2();

        Board board = BoardFactory.createBoard(userFactory);
        Comment comment = ReplyCommentFactory.createComment(user1, board);
        ReplyComment replyComment = ReplyCommentFactory.createReplyComment(user1, comment);

        ReplyCommentEditRequest request = ReplyCommentEditRequest.builder()
                .replyComment("대댓글수정")
                .build();


        given(userRepository.findByLoginId(user2.getLoginId())).willReturn(Optional.of(user2));

        assertThatThrownBy(() -> replyCommentService.ReplyCommentEdit(replyComment.getId(), request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("작성자만 가능합니다.");

        verify(replyCommentRepository, never()).save(replyComment); // 수정 메서드 x
    }

}
