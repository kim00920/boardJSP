package com.example._Board.config.rabbitMQ;

import com.example._Board.board.domain.Board;
import com.example._Board.board.repository.BoardRepository;
import com.example._Board.error.BusinessException;
import com.example._Board.user.controller.request.LikeNotificationMessage;
import com.example._Board.user.domain.User;
import com.example._Board.user.repository.UserRepository;
import com.example._Board.user.service.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.example._Board.error.ErrorCode.NOT_FOUND_BOARD;
import static com.example._Board.error.ErrorCode.NOT_FOUND_USER;

@Slf4j
@Component
@RequiredArgsConstructor
public class LikeConsumer {

    private final NotificationService notificationService;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "like-notification-queue")
    public void receiveLikeNotification(String messageJson) {
        try {
            // 자바로 역직렬화
            LikeNotificationMessage message = objectMapper.readValue(messageJson, LikeNotificationMessage.class);

            Long senderId = message.getUserId();
            Long boardId = message.getBoardId();
            Long receiverId = message.getReceiverId();

            User sender = userRepository.findById(senderId)
                    .orElseThrow(() -> new BusinessException(NOT_FOUND_USER));

            User receiver = userRepository.findById(receiverId)
                    .orElseThrow(() -> new BusinessException(NOT_FOUND_USER));

            Board board = boardRepository.findById(boardId)
                    .orElseThrow(() -> new BusinessException(NOT_FOUND_BOARD));

            // 알림 메세지
            String alert = sender.getName() + " 님이 회원님의 게시글(" + board.getId() + "번)에 좋아요를 눌렀습니다.";
            notificationService.notificationCreate(senderId, receiverId, boardId, alert);

            log.info("송신자: {}", sender.getName());
            log.info("수신자: {}", receiver.getName());
            log.info("송신자 게시글: {}", board.getLikeCount());
            log.info("알림 메시지 처리 완료: {}", alert);

        } catch (Exception e) {
            log.warn("알림 JSON 파싱 또는 처리 실패: {}", e.getMessage());
        }
    }
}
