package com.example._Board.user.domain;

import com.example._Board.board.domain.Board;
import com.example._Board.config.BaseTimeEntity;
import com.example._Board.user.controller.request.UserEditRequest;
import com.example._Board.user.controller.request.UserSignupRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(nullable = false, length = 20, unique = true)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false, length = 50)
    @Email
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;


    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Builder.Default
    private List<Board> boards = new ArrayList<>();


    // 회원 생성
    public static User createUser(UserSignupRequest userSignupRequest, PasswordEncoder passwordEncoder) {
        return User.builder()
                .loginId(userSignupRequest.getLoginId())
                .password(passwordEncoder.encode(userSignupRequest.getPassword()))
                .name(userSignupRequest.getName())
                .email(userSignupRequest.getEmail())
                .build();
    }

    // 회원 수정
    public User EditUser(UserEditRequest userEditRequest, PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(userEditRequest.getPassword());
        this.email = userEditRequest.getEmail();

        return this;
    }

    // 비밀번호 변경
    public void setPassword(String password) {
        this.password = password;
    }

    public void changeRole(Role role) {
        this.role = role;
    }
}
