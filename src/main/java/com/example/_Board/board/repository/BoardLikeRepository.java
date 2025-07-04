package com.example._Board.board.repository;

import com.example._Board.board.domain.Board;
import com.example._Board.board.domain.BoardLike;
import com.example._Board.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {
    boolean existsByBoardAndUser(Board board, User user);

    Optional<BoardLike> findByBoardAndUser(Board board, User user);

    List<BoardLike> findAllByBoard(Board board);
}
