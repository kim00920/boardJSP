package com.example._Board.user.controller.request;

import com.example._Board.user.domain.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSignupRequest {

    @NotEmpty(message = "로그인 아이디를 입력하세요")
    private String loginId;

    @NotEmpty(message = "비밀번호를 입력하세요")
    private String password;

    @NotEmpty(message = "이름을 입력하세요")
    private String name;

    @NotEmpty(message = "이메일을 입력하세요")
    @Email(message = "이메일 형식으로 입력하세요")
    private String email;

}
