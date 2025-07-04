package com.example.boardtest.factory;


import com.example._Board.board.domain.Board;
import com.example._Board.board.domain.Category;

public class BoardFactory {
    public static Board createBoard(UserFactory userFactory) {

        return Board.builder()
                .id(1L)
                .user(userFactory.createUser1())
                .category(new Category(1L, "일상"))
                .title("제목1")
                .content("내용1")
                .likeCount(10)
                .viewCount(100)
                .isNotice(false)
                .build();
    }

    public static Board createBoarNoticeTrue(UserFactory userFactory) {

        return Board.builder()
                .id(2L)
                .user(userFactory.createUser1())
                .category(new Category(2L, "스포츠"))
                .title("제목2")
                .content("내용2")
                .likeCount(0)
                .viewCount(0)
                .isNotice(true)
                .build();
    }
}
