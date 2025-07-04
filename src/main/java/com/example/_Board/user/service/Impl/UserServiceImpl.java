package com.example._Board.user.service.Impl;

import com.example._Board.config.security.CustomUserDetails;
import com.example._Board.error.BusinessException;
import com.example._Board.user.controller.request.UserEditRequest;
import com.example._Board.user.controller.request.UserPasswordEditRequest;
import com.example._Board.user.controller.request.UserSignupRequest;
import com.example._Board.user.controller.response.UserResponse;
import com.example._Board.user.domain.DeleteUser;
import com.example._Board.user.domain.Role;
import com.example._Board.user.domain.User;
import com.example._Board.user.repository.DeleteUserRepository;
import com.example._Board.user.repository.UserRepository;
import com.example._Board.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example._Board.error.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DeleteUserRepository deleteUserRepository;

    // 내 정보 조회
    @Override
    @Transactional(readOnly = true)
    public UserResponse findByMyInfo() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User user = userRepository.findByLoginId(authentication.getName()).orElseThrow(() -> new BusinessException(NOT_FOUND_USER));

        User user = getUser();
        return UserResponse.toResponse(user);
    }

    // 회원 생성
    @Override
    public void userSignup(UserSignupRequest userSignupRequest) {
        log.info("회원 생성 {}", userSignupRequest);

        DuplicatedLoginIdCheck(userRepository.findByLoginId(userSignupRequest.getLoginId()).isPresent());

        User user = User.createUser(userSignupRequest, passwordEncoder);
        log.info("회원 생성 {}", user);

        userRepository.save(user);
        log.info("회원 생성 성공");
    }

    // 회원가입 중복 체크
    @Override
    public void loginIdDuplicateCheck(String loginId) {
        log.info("로그인 아이디 중복 체크 {}", loginId);

        DuplicatedLoginIdCheck(userRepository.findByLoginId(loginId).isPresent());

        log.info("로그인 아이디 중복 체크 성공");
    }

    // 회원 비밀번호 변경
    @Override
    public void userPasswordEdit(UserPasswordEditRequest userPasswordEdit) {
        User user = getUser();

        if (!passwordEncoder.matches(userPasswordEdit.getCurrentPassword(), user.getPassword())) {
            throw new BusinessException(NOT_MATCH_PASSWORD);
        }

        String newPassword = userPasswordEdit.getNewPassword();
        user.setPassword(newPassword);
    }

    // 회원 수정
    @Override
    public void userEdit(UserEditRequest userEditRequest) {
        User user = getUser();
        log.info("회원 수정 {}", userEditRequest);

        user.EditUser(userEditRequest, passwordEncoder);
        log.info("회원 수정 성공");
    }

    // 회원 삭제 (삭제 전 deleteUser 에 저장 후 삭제)
    @Override
    public void userDelete() {
        User user = getUser();
        log.info("회원 삭제 {}", user);

        DeleteUser deleteUser = DeleteUser.createDeleteUser(user);
        deleteUserRepository.save(deleteUser);

        userRepository.delete(user);
        log.info("회원 삭제 성공");
    }

    // 회원 단건 조회
    @Override
    @Transactional(readOnly = true)
    public UserResponse findOneUser(Long id) {
        log.info("회원 단건 조회 : {}", id);
        User user = userRepository.findById(id).orElseThrow(() -> new BusinessException(NOT_FOUND_USER));

        return UserResponse.toResponse(user);
    }

    // 회원 전체 조회
    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> findAllUser() {

        List<User> userList = userRepository.findAll();

        log.info("회원 전체 조회 {} 명", userList.size());

        return userList.stream()
                .map(UserResponse::toResponse)
                .toList();
    }


    // 회원 로그인 성공 시 객체 반환
    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        log.info("로그인 시도: 로그인 회원 {}", loginId);

        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new BusinessException(NOT_FOUND_USER));

        if (loginId.equals("admin")) {
            user.changeRole(Role.ADMIN);
        } else {
            user.changeRole(Role.USER);
        }

        log.info("로그인 성공: 로그인 회원 {}", user.getLoginId());
        return new CustomUserDetails(user);
    }

    // 회원 객체 반환
    private User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginId = authentication.getName();

        log.info("로그인 회원 {}", loginId);

        return userRepository.findByLoginId(loginId).orElseThrow(
                () -> new BusinessException(NOT_FOUND_USER));
    }

    // 로그인 아이디 중복 체크
    private void DuplicatedLoginIdCheck(boolean duplicatedCheck) {
        if (duplicatedCheck) {
            throw new BusinessException(DUPLICATED_LOGIN_ID);
        }
    }


}
