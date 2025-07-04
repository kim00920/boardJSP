package com.example._Board.user.controller.response;

import com.example._Board.user.domain.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private String loginId;         //회원 ID
    private String name;            //이름
    private String email;           //이메일
    private String role;
    private LocalDateTime createAt;

    public static UserResponse toResponse(User user) {
        return UserResponse.builder()
                .loginId(user.getLoginId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .createAt(user.getCreatedAt())
                .build();
    }
}
