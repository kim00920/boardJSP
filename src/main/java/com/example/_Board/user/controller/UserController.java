package com.example._Board.user.controller;

import com.example._Board.user.controller.request.UserPasswordEditRequest;
import io.swagger.v3.oas.annotations.Operation;
import com.example._Board.user.controller.request.UserEditRequest;
import com.example._Board.user.controller.request.UserSignupRequest;
import com.example._Board.user.controller.response.UserResponse;
import com.example._Board.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Slf4j
public class UserController {

    private final UserService userService;


    @Operation(summary = "회원 가입")
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void userSignup(@RequestBody @Valid UserSignupRequest request) {
        userService.userSignup(request);
    }


    @Operation(summary = "로그인 아이디 중복 체크")
    @PostMapping("/exist")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void userLoginIdDuplicateCheck(String loginId) {
        userService.loginIdDuplicateCheck(loginId);
    }


    @Operation(summary = "내 정보 가져오기")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public UserResponse findByDetailMyInfo() {
        return userService.findByMyInfo();
    }


    @Operation(summary = "회원 수정")
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void userEdit(@RequestBody @Valid UserEditRequest request) {
        userService.userEdit(request);
    }


    @Operation(summary = "회원 탈퇴")
    @DeleteMapping("/signout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void userDelete() {
        userService.userDelete();
    }

    @Operation(summary = "회원 비밀번호 변경")
    @PutMapping("/password")
    @ResponseStatus(HttpStatus.OK)
    public void userEditPassword(@RequestBody UserPasswordEditRequest request) {
        userService.userPasswordEdit(request);
    }

    @Operation(summary = "회원 단건 조회")
    @PostMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse findOneUser(@PathVariable("userId") Long userId) {
        return userService.findOneUser(userId);
    }

    @Operation(summary = "회원 전체 조회")
    @PostMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponse> findAllUser() {
        return userService.findAllUser();
    }

}
