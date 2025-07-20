package com.example._Board.config.rabbitMQ;

import com.example._Board.user.controller.request.LikeNotificationMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikeEventProducer {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    private static final String EXCHANGE_NAME = "like-exchange";
    private static final String ROUTING_KEY = "like.notification";

    public void sendLikeNotification(Long boardId, Long userId, Long receiverId) {

        LikeNotificationMessage message = LikeNotificationMessage.builder()
                .boardId(boardId)
                .userId(userId)
                .receiverId(receiverId)
                .build();

        try {
            String jsonMessage = objectMapper.writeValueAsString(message); // json 직렬화
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, jsonMessage);
            log.info("convertAndSend 성공");
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 직렬화 실패", e);
        }
    }
}