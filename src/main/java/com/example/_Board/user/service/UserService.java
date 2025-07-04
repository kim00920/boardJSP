package com.example._Board.user.service;

import com.example._Board.user.controller.request.UserEditRequest;
import com.example._Board.user.controller.request.UserPasswordEditRequest;
import com.example._Board.user.controller.request.UserSignupRequest;
import com.example._Board.user.controller.response.UserResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    // 내 정보 찾기
    UserResponse findByMyInfo();

    // 회원 생성
    void userSignup(UserSignupRequest userSignupRequest);

    // 회원 중복체크
    void loginIdDuplicateCheck(String loginId);

    // 비밀번호 변경
    void userPasswordEdit(UserPasswordEditRequest userPasswordEdit);

    // 회원 수정
    void userEdit(UserEditRequest userEditRequest);

    // 회원 삭제
    void userDelete();

    // 회원 단건 조회
    UserResponse findOneUser(Long id);

    // 회원 전체 조회
    List<UserResponse> findAllUser();
}
