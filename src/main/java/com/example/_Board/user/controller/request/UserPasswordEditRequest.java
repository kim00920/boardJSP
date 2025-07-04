package com.example._Board.user.controller.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPasswordEditRequest {

    private String currentPassword;
    private String newPassword;
}
