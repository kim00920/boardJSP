package com.example._Board.board.repository;

import com.example._Board.board.domain.Board;
import com.example._Board.board.domain.Category;
import com.example._Board.comment.domain.Comment;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardPageCustomRepository {


    List<Board> findAllByCategory(Category category);

    Page<Board> findByIsNoticeFalseAndTitleContaining(String keyword, Pageable pageable);


    // 비관 락
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b FROM Board b WHERE b.id = :id")
    Optional<Board> findByIdWithPessimisticLock(@Param("id") Long id);


    @Query("SELECT b FROM Board b LEFT JOIN FETCH b.comments c LEFT JOIN FETCH c.user WHERE b.id = :boardId")
    Optional<Board> findByIdWithCommentsAndUsers(@Param("boardId") Long boardId);


    @Query("""
                SELECT DISTINCT b FROM Board b
                LEFT JOIN FETCH b.comments c
                LEFT JOIN FETCH c.user
                LEFT JOIN FETCH c.replyComments rc
                LEFT JOIN FETCH rc.comment
                WHERE b.id = :boardId
            """)
    Optional<Board> findByIdWithAll(@Param("boardId") Long boardId);

    @Modifying
    @Query("UPDATE Board b SET b.commentCount = :count WHERE b.id = :boardId")
    void updateCommentCount(@Param("boardId") Long boardId, @Param("count") long count);


}
