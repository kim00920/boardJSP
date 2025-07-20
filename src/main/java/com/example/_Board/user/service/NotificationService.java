package com.example._Board.user.service;

import com.example._Board.user.controller.response.NotificationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationService {

    // 특정 사용자가 아직 안읽은 알림 개수
    int notificationUnReadCnt(Long userId);

    // 특정 사용자 알림 전체 조회 + 페이징
    Page<NotificationResponse> notificationFindAll(Long userId, Pageable pageable);

    // 알림 읽음 처리
    void notificationRead(Long notificationId);

    // 알림 생성
    void notificationCreate(Long senderId, Long receiverId, Long boardId, String message);

    // 알림 삭제
    void notificationDelete(Long notificationId);
}
