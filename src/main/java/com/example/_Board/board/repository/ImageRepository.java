package com.example._Board.board.repository;

import com.example._Board.board.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByBoardId(Long id);
}
