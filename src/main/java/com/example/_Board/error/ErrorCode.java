package com.example._Board.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // 회원
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),
    DUPLICATED_LOGIN_ID(HttpStatus.BAD_REQUEST, "존재하고 있는 아이디 입니다."),
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "로그인된 사용자만 접근 가능합니다."),
    NOT_EQUALS_PASSWORD(HttpStatus.BAD_REQUEST, "현재 비밀번호가 일치하지 않습니다."),
    NOT_EQUALS_PASSWORD_CONFIRM(HttpStatus.BAD_REQUEST, "새 비밀번호 확인과 새 비밀번호가 일치하지 않습니다."),
    DUPLICATED_PASSWORD(HttpStatus.BAD_REQUEST, "같은 비밀번호는 사용 할 수 없습니다."),
    NOT_MATCH_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

    // 탈퇴 회원
    NOT_FOUND_DELETEUSER(HttpStatus.BAD_REQUEST, "존재하지 않는 탈퇴 회원입니다."),
    // 카테고리
    NOT_FOUND_CATEGORY(HttpStatus.NOT_FOUND, "존재하지 않는 카테고리입니다."),
    CATEGORY_NAME_DUPLICATED(HttpStatus.BAD_REQUEST, "이미 존재하는 카테고리 입니다."),
    NOT_LIKE_BOARD(HttpStatus.BAD_REQUEST, "좋아요를 누르지 않았습니다."),
    DUPLICATED_LIKE_BOARD(HttpStatus.BAD_REQUEST, "이미 좋아요를 눌렀습니다."),

    // 게시글
    NOT_FOUND_BOARD(HttpStatus.NOT_FOUND, "존재하지 않는 게시글입니다."),
    NOT_MATCH_COMMENT(HttpStatus.BAD_REQUEST, "작성자만 가능합니다."),

    // 댓글
    NOT_FOUND_COMMENT(HttpStatus.NOT_FOUND, "존재하는 댓글이 없습니다."),
    CANT_DELETE_COMMENT(HttpStatus.BAD_REQUEST, "삭제할 수 없는 댓글입니다."),

    // 대댓글
    NOT_FOUND_REPLY_COMMENT(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."),
    NOT_WRITE_REPLY_COMMENT(HttpStatus.BAD_REQUEST, "작성한 댓글이 아닙니다."),

    // 카테고리
    CATEGORY_EXIST_BOARD(HttpStatus.BAD_REQUEST, "카테고리에 속한 게시글이 존재합니다.");

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    private final HttpStatus httpStatus;
    private final String message;
}
