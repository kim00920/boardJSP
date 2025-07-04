package com.example._Board.user.service;

import com.example._Board.user.controller.response.DeleteUserResponse;
import com.example._Board.user.repository.DeleteUserRepository;

import java.util.List;

public interface DeleteUserService {


    // 탈퇴한 회원 전체 조회
    List<DeleteUserResponse> deleteUserFindAll();

    // 탈퇴한 회원 단건 조회
    DeleteUserResponse deleteUserFindOne(Long id);

    // 탈퇴한 회원 삭제
    void deleteUserDelete(Long deleteUserId);
}
