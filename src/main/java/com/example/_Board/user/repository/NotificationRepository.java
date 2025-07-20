package com.example._Board.user.repository;

import com.example._Board.user.domain.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // 알림 수신자 기준 전체 알림 조회 >> 생성일자순으로
    Page<Notification> findByReceiverIdOrderByCreatedAtDesc(Long receiverId, Pageable pageable);

    // 알림 수신자의 읽지 않은 알림 개수
    int countByReceiverIdAndAlreadyReadFalse(Long receiverId);

    // 중복 알림 방지용임
    boolean existsBySenderIdAndReceiverIdAndBoardId(Long senderId, Long receiverId, Long boardId);
}
