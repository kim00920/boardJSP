package com.example.boardtest.factory;


import com.example._Board.user.controller.request.UserSignupRequest;
import com.example._Board.user.domain.Role;
import com.example._Board.user.domain.User;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserFactory {

    PasswordEncoder passwordEncoder;

    public UserFactory(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser1() {
        return User.builder()
                .id(1L)
                .loginId("loginId")
                .password(passwordEncoder.encode("1234"))
                .name("name")
                .email("user@test.com")
                .role(Role.USER)
                .build();
    }

    public User createUser2() {
        return User.builder()
                .id(2L)
                .loginId("loginId")
                .password(passwordEncoder.encode("1234"))
                .name("name1")
                .email("use1r@test.com")
                .role(Role.USER)
                .build();
    }

    public UserSignupRequest createUserRequest() {

        return UserSignupRequest.builder()
                .loginId("loginId")
                .password(passwordEncoder.encode("1234"))
                .name("name")
                .email("user@test.com")
                .build();
    }


}
