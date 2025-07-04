package com.example.boardtest.user.service;

import com.example._Board.error.BusinessException;
import com.example._Board.user.controller.request.UserEditRequest;
import com.example._Board.user.controller.request.UserSignupRequest;
import com.example._Board.user.controller.response.UserResponse;
import com.example._Board.user.domain.DeleteUser;
import com.example._Board.user.domain.Role;
import com.example._Board.user.domain.User;
import com.example._Board.user.repository.DeleteUserRepository;
import com.example._Board.user.repository.UserRepository;
import com.example._Board.user.service.Impl.UserServiceImpl;
import com.example.boardtest.factory.UserFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("회원 단위테스트")
public class UserServiceTest {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    DeleteUserRepository deleteUserRepository;

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
    @DisplayName("회원 가입")
    void 테스트() {
        UserFactory userFactory = new UserFactory(passwordEncoder);
        User user = userFactory.createUser1();
        Assertions.assertEquals(1L, user.getId());

        UserSignupRequest userSignupRequest = userFactory.createUserRequest();

        userService.userSignup(userSignupRequest);

        verify(userRepository).save(any());
    }

    @Test
    @DisplayName("내 정보 조회")
    void userMyInfo() {
        UserFactory userFactory = new UserFactory(passwordEncoder);
        User user = userFactory.createUser1();

        when(userRepository.findByLoginId("loginId")).thenReturn(Optional.of(user));

        UserResponse userInfo = userService.findByMyInfo();

        assertThat(userInfo).isNotNull();
        assertThat(userInfo.getLoginId()).isEqualTo("loginId");
        assertThat(userInfo.getName()).isEqualTo("name");
    }

    @Test
    @DisplayName("중복된 아이디가 있으면 예외 발생")
    void loginIdDuplicateCheck() {
        UserFactory userFactory = new UserFactory(passwordEncoder);
        User user = userFactory.createUser1();

        given(userRepository.findByLoginId("loginId"))
                .willReturn(Optional.of(user));

        assertThrows(BusinessException.class,
                () -> {
                   userService.loginIdDuplicateCheck("loginId");
                });
    }

    @Test
    @DisplayName("회원 수정")
    void userEdit() {
        UserFactory userFactory = new UserFactory(passwordEncoder);
        User user = userFactory.createUser1();
        given(userRepository.findByLoginId("loginId"))
                .willReturn(Optional.of(user));

        UserEditRequest userEditRequest = UserEditRequest.builder()
                .password("12345")
                .email("edit@test.com")
                .build();

        userService.userEdit(userEditRequest);

        assertThat(user.getEmail()).isEqualTo("edit@test.com");

    }

    @Test
    @DisplayName("회원 탈퇴")
    void userDelete() {
        UserFactory userFactory = new UserFactory(passwordEncoder);
        User user = userFactory.createUser1();

        given(userRepository.findByLoginId("loginId"))
                .willReturn(Optional.of(user));

        userService.userDelete();

        assertThat(userRepository.findById(user.getId())).isEmpty();

        verify(deleteUserRepository, times(1)).save(any(DeleteUser.class));
        verify(userRepository, times(1)).delete(user);

    }

    @Test
    @DisplayName("회원 로그인 성공")
    void userLoginSuccess() {
        UserFactory userFactory = new UserFactory(passwordEncoder);
        User user = userFactory.createUser1();

        given(userRepository.findByLoginId("loginId"))
                .willReturn(Optional.of(user));

        userService.loadUserByUsername("loginId");

        assertThat(user).isNotNull();
        assertThat(user.getLoginId()).isEqualTo("loginId");
        assertThat(user.getName()).isEqualTo("name");
        assertThat(user.getRole()).isEqualTo(Role.USER);
        assertThat(user.getEmail()).isEqualTo("user@test.com");
    }

    @Test
    @DisplayName("회원 로그인 실패")
    void userLoginFail() {
        given(userRepository.findByLoginId("loginId1"))
                .willReturn(Optional.empty());

        assertThrows(BusinessException.class,
                () -> {
                    userService.loadUserByUsername("loginId1");
                });

    }
}
