package com.example._Board.user.service.Impl;

import com.example._Board.error.BusinessException;
import com.example._Board.error.ErrorCode;
import com.example._Board.user.controller.response.DeleteUserResponse;
import com.example._Board.user.domain.DeleteUser;
import com.example._Board.user.repository.DeleteUserRepository;
import com.example._Board.user.service.DeleteUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example._Board.error.ErrorCode.NOT_FOUND_DELETEUSER;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteUserServiceImpl implements DeleteUserService {

    private final DeleteUserRepository deleteUserRepository;

    // 탈퇴한 회원 전체 조회
    @Override
    @Transactional(readOnly = true)
    public List<DeleteUserResponse> deleteUserFindAll() {
        List<DeleteUser> deleteUserList = deleteUserRepository.findAll();

        return DeleteUserResponse.toResponse(deleteUserList);
    }

    // 탈퇴한 회원 단건 조회
    @Override
    @Transactional(readOnly = true)
    public DeleteUserResponse deleteUserFindOne(Long id) {
        DeleteUser deleteUser = deleteUserRepository.findById(id).orElseThrow(() -> new BusinessException(NOT_FOUND_DELETEUSER));

        return DeleteUserResponse.toOneResponse(deleteUser);
    }

    // 탈퇴한 회원 삭제
    @Override
    public void deleteUserDelete(Long deleteUserId) {
        DeleteUser deleteUser = deleteUserRepository.findById(deleteUserId).orElseThrow(
                () -> new BusinessException(NOT_FOUND_DELETEUSER));

        deleteUserRepository.delete(deleteUser);
    }
}
