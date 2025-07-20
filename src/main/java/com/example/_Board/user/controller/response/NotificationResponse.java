package com.example._Board.user.controller.response;

import com.example._Board.user.domain.Notification;
import lombok.*;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponse {

    private Long id;

    // 알림을 보낸 사람
    private Long senderId;
    private String senderName;

    // 알림을 받은 사람
    private Long receiverId;
    private String receiverName;

    private String message;
    private boolean read;
    private String formattedCreatedAt;

    public static NotificationResponse toDto(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .senderId(notification.getSender().getId())
                .senderName(notification.getSender().getName())
                .receiverId(notification.getReceiver().getId())
                .receiverName(notification.getReceiver().getName())
                .message(notification.getMessage())
                .read(notification.isAlreadyRead())
                .formattedCreatedAt(
                        notification.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                )
                .build();
    }
}
