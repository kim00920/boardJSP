package com.example._Board.config.rabbitMQ;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
public class RabbitConfig {

    @Bean
    public TopicExchange likeExchange() {
        return new TopicExchange("like-exchange");
    }

    @Bean
    public Queue likeQueue() {
        return QueueBuilder.durable("like-notification-queue").build();
    }

    @Bean
    public Binding binding(Queue likeQueue, TopicExchange likeExchange) {
        return BindingBuilder.bind(likeQueue) // 최종 바인딩 경로임 like-notification-queue
                .to(likeExchange) // 저장 장소
                .with("like.notification"); // 라우팅 키 값
    }
}