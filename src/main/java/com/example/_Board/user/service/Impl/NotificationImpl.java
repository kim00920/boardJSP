
package com.example._Board.user.service.Impl;

import com.example._Board.board.domain.Board;
import com.example._Board.board.repository.BoardRepository;
import com.example._Board.error.BusinessException;
import com.example._Board.user.controller.response.NotificationResponse;
import com.example._Board.user.domain.Notification;
import com.example._Board.user.domain.User;
import com.example._Board.user.repository.NotificationRepository;
import com.example._Board.user.repository.UserRepository;
import com.example._Board.user.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.example._Board.error.ErrorCode.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class NotificationImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;


    // 회원이 안본 알림 개수
    @Override
    @Transactional(readOnly = true)
    public int notificationUnReadCnt(Long userId) {
        return notificationRepository.countByReceiverIdAndAlreadyReadFalse(userId);
    }

    // 알림 전체 보기 + 페이징
    @Override
    @Transactional(readOnly = true)
    public Page<NotificationResponse> notificationFindAll(Long userId, Pageable pageable) {
        Page<Notification> notifications = notificationRepository.findByReceiverIdOrderByCreatedAtDesc(userId, pageable);
        return notifications.map(NotificationResponse::toDto);
    }

    // 알림목록에서 누르면 읽음 처리하기
    @Override
    public void notificationRead(Long notificationId) {
        User user = getUser();

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new BusinessException(NOT_FOUND_NOTIFICATION));

        if (!notification.getReceiver().getId().equals(user.getId())) {
            throw new BusinessException(NOT_MATCH_NOTIFICATION);
        }


        notification.setAlreadyRead(true);
        //notificationRepository.save(notification);
    }

    // 알림 생성
    @Override
    public void notificationCreate(Long senderId, Long receiverId, Long boardId, String message) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new BusinessException(NOT_FOUND_USER));

        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new BusinessException(NOT_FOUND_USER));


        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BusinessException(NOT_FOUND_BOARD));

        // 중복 알림
        if (notificationRepository.existsBySenderIdAndReceiverIdAndBoardId(senderId, receiverId, boardId)) {
            log.info("중복된 회원이므로 알림생성은 안됨");
            return;
        }


        Notification notification = Notification.builder()
                .sender(sender)
                .receiver(receiver)
                .board(board)
                .message(message)
                .alreadyRead(false)
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);
    }

    // 알림 삭제
    @Override
    public void notificationDelete(Long notificationId) {
        User user = getUser();

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new BusinessException(NOT_FOUND_NOTIFICATION));

        if (!notification.getReceiver().getId().equals(user.getId())) {
            throw new BusinessException(NOT_MATCH_NOTIFICATION);
        }

        notificationRepository.delete(notification);
    }

    // 회원 객체 반환
    private User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginId = authentication.getName();

        log.info("로그인 회원 {}", loginId);

        return userRepository.findByLoginId(loginId).orElseThrow(
                () -> new BusinessException(NOT_FOUND_USER));
    }
}
