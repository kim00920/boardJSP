package com.example._Board.comment.repository;

import com.example._Board.comment.domain.ReplyComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReplyCommentRepository extends JpaRepository<ReplyComment, Long> {
    Optional<ReplyComment> findByIdAndUserId(Long replyCommentId, Long userId);
}
