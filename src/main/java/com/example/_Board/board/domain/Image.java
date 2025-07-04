package com.example._Board.board.domain;

import com.example._Board.config.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "images")
public class Image extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    private String fileUrl;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Builder
    public Image(String fileUrl, Board board) {
        this.fileUrl = fileUrl;
        this.board = board;
    }

    @Builder
    public Image(Long id, String fileUrl, Board board) {
        this.id = id;
        this.fileUrl = fileUrl;
        this.board = board;
    }

    // 연관관계
    public void setBoard(Board board) {
        this.board = board;
        board.getImages().add(this);
    }
}