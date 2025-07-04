package com.example._Board.user.controller;

import com.example._Board.user.controller.response.DeleteUserResponse;
import com.example._Board.user.service.DeleteUserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/deleteUser")
public class DeleteUserController {

    private final DeleteUserService deleteUserService;

    @Operation(summary = "탈퇴한 회원 전체 조회")
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<DeleteUserResponse> deleteUserFindAll() {
        return deleteUserService.deleteUserFindAll();
    }

    @Operation(summary = "탈퇴한 회원 단건 조회")
    @GetMapping("/{deleteUserId}")
    @ResponseStatus(HttpStatus.OK)
    public DeleteUserResponse deleteUserFindOne(@PathVariable("deleteUserId") Long deleteUserId) {
        return deleteUserService.deleteUserFindOne(deleteUserId);
    }

    @Operation(summary = "탈퇴한 회원 삭제")
    @DeleteMapping("/{deleteUserId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUserDelete(@PathVariable("deleteUserId") Long deleteUserId) {
        deleteUserService.deleteUserDelete(deleteUserId);
    }

}
