package com.example.boardtest.comment.service;

import com.example._Board.board.domain.Board;
import com.example._Board.board.repository.BoardRepository;
import com.example._Board.comment.controller.request.CommentCreateRequest;
import com.example._Board.comment.controller.request.CommentEditRequest;
import com.example._Board.comment.controller.response.CommentResponse;
import com.example._Board.comment.domain.Comment;
import com.example._Board.comment.domain.ReplyComment;
import com.example._Board.comment.repository.CommentRepository;
import com.example._Board.comment.service.Impl.CommentServiceImpl;
import com.example._Board.error.BusinessException;
import com.example._Board.user.domain.User;
import com.example._Board.user.repository.UserRepository;
import com.example.boardtest.factory.BoardFactory;
import com.example.boardtest.factory.CommentFactory;
import com.example.boardtest.factory.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("댓글 테스트")
public class CommentServiceTest {

    @InjectMocks
    CommentServiceImpl commentService;

    @Mock
    BoardRepository boardRepository;

    @Mock
    UserRepository userRepository;

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
    @DisplayName("댓글 생성")
    void createComment() {
        UserFactory userFactory = new UserFactory(passwordEncoder);
        Board board = BoardFactory.createBoard(userFactory);
        User user = userFactory.createUser1();

        Comment comment = CommentFactory.createComment(board, user);

        CommentCreateRequest commentCreateRequest = CommentCreateRequest.builder()
                .comment(comment.getComment())
                .build();

        given(userRepository.findByLoginId(any())).willReturn(Optional.of(board.getUser()));
        given(boardRepository.findById(any())).willReturn(Optional.of(board));

        commentService.commentCreate(board.getId(), commentCreateRequest);

        assertThat(comment.getComment()).isEqualTo("댓글");
        assertThat(comment.getUser().getId()).isEqualTo(1L);
        assertThat(comment.getBoard().getTitle()).isEqualTo("제목1");

        verify(commentRepository).save(any());
        verify(boardRepository).findById(any());
    }

    @DisplayName("특정 게시글 댓글 전체 조회 (페이징 + 대댓글)")
    @Test
    void commentFindAll() {
        // given
        UserFactory userFactory = new UserFactory(passwordEncoder);
        User user = userFactory.createUser1();
        Board board = BoardFactory.createBoard(userFactory);

        Comment parentComment = CommentFactory.createComment(board, user);

        ReplyComment reply1 = ReplyComment.builder()
                .replyComment("대댓글1")
                .comment(parentComment)
                .build();

        ReplyComment reply2 = ReplyComment.builder()
                .replyComment("대댓글2")
                .comment(parentComment)
                .build();

        parentComment.getReplyComments().add(reply1);
        parentComment.getReplyComments().add(reply2);

        List<Comment> commentList = List.of(parentComment);
        Page<Comment> commentPage = new PageImpl<>(commentList);

        given(boardRepository.findById(board.getId())).willReturn(Optional.of(board));
        given(commentRepository.findAllByBoard(eq(board), any(Pageable.class))).willReturn(commentPage);

        Pageable pageable = PageRequest.of(0, 10);
        Page<CommentResponse> result = commentService.commentFindAllByBoard(board.getId(), pageable);

        assertThat(result.getContent()).hasSize(1);
        CommentResponse commentResponse = result.getContent().get(0);
        assertThat(commentResponse.getComment()).isEqualTo("댓글");

        assertThat(commentResponse.getReplyResponse()).hasSize(2);
        assertThat(commentResponse.getReplyResponse().get(0).getReplyComment()).isEqualTo("대댓글1");
        assertThat(commentResponse.getReplyResponse().get(1).getReplyComment()).isEqualTo("대댓글2");

        verify(boardRepository).findById(board.getId());
        verify(commentRepository).findAllByBoard(eq(board), eq(pageable));
    }

    @DisplayName("댓글 수정 성공 (작성자 O)")
    @Test
    void commentEditSuccess() {
        UserFactory userFactory = new UserFactory(passwordEncoder);
        Board board = BoardFactory.createBoard(userFactory);
        User user = userFactory.createUser1();
        Comment comment = CommentFactory.createComment(board, user);

        CommentEditRequest commentEditRequest = CommentEditRequest.builder()
                .comment("댓글 수정")
                .build();

        given(userRepository.findByLoginId(any())).willReturn(Optional.of(user));
        given(commentRepository.findById(comment.getId())).willReturn(Optional.of(comment));


        commentService.commentEdit(comment.getId(), commentEditRequest);

        assertThat(comment.getComment()).isEqualTo("댓글 수정");
    }

    @DisplayName("댓글 수정 실패 (작성자 X))")
    @Test
    void commentEditFail() {
        // given
        UserFactory userFactory = new UserFactory(passwordEncoder);
        User writer = userFactory.createUser1(); // 댓글 작성자
        User anotherUser = userFactory.createUser2(); // 로그인 유저

        Board board = BoardFactory.createBoard(userFactory);
        Comment comment = CommentFactory.createComment(board, writer);

        CommentEditRequest editRequest = new CommentEditRequest("수정 시도");

        given(userRepository.findByLoginId(any())).willReturn(Optional.of(anotherUser));
        given(commentRepository.findById(comment.getId())).willReturn(Optional.of(comment));

        assertThatThrownBy(() -> commentService.commentEdit(comment.getId(), editRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("작성자만 가능합니다.");

        verify(commentRepository).findById(comment.getId());
    }

    @DisplayName("댓글 삭제 성공 (작성자 O)")
    @Test
    void commentDeleteSuccess() {
        UserFactory userFactory = new UserFactory(passwordEncoder);
        User writer = userFactory.createUser1(); // 댓글 작성자
        Board board = BoardFactory.createBoard(userFactory);
        Comment comment = CommentFactory.createComment(board, writer);

        given(userRepository.findByLoginId(any())).willReturn(Optional.of(writer));
        given(commentRepository.findById(comment.getId())).willReturn(Optional.of(comment));

        commentService.commentDelete(comment.getId());

        verify(commentRepository).delete(comment);
    }


    @DisplayName("댓글 삭제 실패 (작성자 X)")
    @Test
    void commentDeleteFail() {
        UserFactory userFactory = new UserFactory(passwordEncoder);
        User writer = userFactory.createUser1();
        User anotherUser = userFactory.createUser2();

        Board board = BoardFactory.createBoard(userFactory);
        Comment comment = CommentFactory.createComment(board, writer);

        given(userRepository.findByLoginId(any())).willReturn(Optional.of(anotherUser));
        given(commentRepository.findById(comment.getId())).willReturn(Optional.of(comment));

        assertThatThrownBy(() -> commentService.commentDelete(comment.getId()))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("작성자만 가능합니다."); // 에러 메시지 검증

        verify(commentRepository, never()).delete(comment); // 댓글 삭제가 호출되지 않았는지 검증임
    }


}
