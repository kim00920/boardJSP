package com.example._Board.user.controller.response;

import com.example._Board.user.domain.DeleteUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeleteUserResponse {

    private String loginId;
    private String name;
    private String email;
    private String role;
    private LocalDateTime deleteAt;

    public static DeleteUserResponse toOneResponse(DeleteUser deleteUser) {
        return DeleteUserResponse.builder()
                .loginId(deleteUser.getLoginId())
                .name(deleteUser.getName())
                .email(deleteUser.getEmail())
                .role(deleteUser.getRole())
                .deleteAt(deleteUser.getDeleteAt())
                .build();
    }

    public static List<DeleteUserResponse> toResponse(List<DeleteUser> deleteUserList) {
        return deleteUserList.stream()
                .map(DeleteUserResponse::toOneResponse)
                .toList();
    }

}
