package com.example._Board.comment.repository;

import com.example._Board.board.domain.Board;
import com.example._Board.comment.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findAllByBoard(Board board, Pageable pageable);


    @Query("SELECT c FROM Comment c JOIN FETCH c.user WHERE c.board = :board")
    Page<Comment> findAllByBoardWithUser(Board board, Pageable pageable);


    @Query("SELECT COUNT(c) FROM Comment c WHERE c.board.id = :boardId")
    long commentCount(@Param("boardId") Long boardId);

    @Query("SELECT COUNT(rc) FROM ReplyComment rc WHERE rc.comment.board.id = :boardId")
    long replyCommentCount(@Param("boardId") Long boardId);

}
