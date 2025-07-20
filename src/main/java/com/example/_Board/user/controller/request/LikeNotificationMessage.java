package com.example._Board.user.controller.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class LikeNotificationMessage {
    private Long userId;
    private Long boardId;
    private Long receiverId;
}