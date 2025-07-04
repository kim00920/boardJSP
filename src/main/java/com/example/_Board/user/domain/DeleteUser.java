package com.example._Board.user.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "delete_users")
@Entity
@Builder
public class DeleteUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String loginId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String role;

    private LocalDateTime deleteAt;


    // 회원 탈퇴시 탈퇴회원으로 등록
    public static DeleteUser createDeleteUser(User user) {
        return DeleteUser.builder()
                .loginId(user.getLoginId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().toString())
                .deleteAt(LocalDateTime.now())
                .build();
    }


}
