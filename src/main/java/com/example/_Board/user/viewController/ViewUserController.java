package com.example._Board.user.viewController;

import com.example._Board.user.controller.request.UserSignupRequest;
import com.example._Board.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class ViewUserController {

    private final UserService userService;

    @GetMapping("/")
    public String homePage() {
        return "home";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    // 회원가입
    @PostMapping("/signup")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public void userSignup(@RequestBody @Valid UserSignupRequest request) {
        userService.userSignup(request);
    }

    // 로그인 유효성 검사
    @PostMapping("/exist")
    @ResponseBody
    public void userLoginIdDuplicateCheck(@RequestParam String loginId) {
        userService.loginIdDuplicateCheck(loginId);
    }


}
