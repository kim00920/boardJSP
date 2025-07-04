package com.example._Board.board.repository;

import com.example._Board.board.controller.response.BoardPageResponse;
import com.example._Board.board.controller.response.BoardResponse;
import com.example._Board.board.controller.response.QBoardPageResponse;

import com.example._Board.board.domain.*;

import com.example._Board.user.domain.QUser;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example._Board.board.domain.QImage.image;

@Repository
@RequiredArgsConstructor
public class BoardPageCustomRepositoryImpl implements BoardPageCustomRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    // 정렬 조건에 따른 페이징 처리
    @Override
    @Transactional(readOnly = true)
    public Page<BoardResponse> findBoardsBySort(BoardSortType sortType, Pageable pageable) {
        QBoard board = QBoard.board;

        JPAQuery<Board> query = queryFactory
                .selectFrom(board)
                .where(board.isNotice.isFalse());

        switch (sortType) {
            case VIEW_DESC -> query.orderBy(board.viewCount.desc()); // 조회수 많은 순
            case VIEW_ASC  -> query.orderBy(board.viewCount.asc());  // 조회수 적은 순
            case LIKE_DESC -> query.orderBy(board.likeCount.desc()); // 좋아요 많은 순
            case LIKE_ASC  -> query.orderBy(board.likeCount.asc());  // 좋아요 적은 순
        }

        List<Board> content = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(board.count())
                .from(board)
                .where(board.isNotice.isFalse())
                .fetchOne();


        List<BoardResponse> responseContent = content.stream()
                .map(BoardResponse::new)
                .collect(Collectors.toList());

        return new PageImpl<>(responseContent, pageable, total);
    }
//    @Override
//    @Transactional(readOnly = true)
//    public Page<BoardResponse> findBoardsBySort(BoardSortType sortType, Pageable pageable) {
//        QBoard board = QBoard.board;
//
//
//        JPAQuery<BoardResponse> query = queryFactory
//                .select(Projections.constructor(
//                        BoardResponse.class,
//                        board.isNotice,
//                        board.likeCount
//                ))
//                .from(board)
//                .where(board.isNotice.isFalse());
//
//
//        switch (sortType) {
//            case VIEW_DESC -> query.orderBy(board.viewCount.desc());
//            case VIEW_ASC -> query.orderBy(board.viewCount.asc());
//            case LIKE_DESC -> query.orderBy(board.likeCount.desc());
//            case LIKE_ASC -> query.orderBy(board.likeCount.asc());
//        }
//
//
//        List<BoardResponse> content = query
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();
//
//
//        JPAQuery<Long> countQuery = queryFactory
//                .select(board.count())
//                .from(board)
//                .where(board.isNotice.isFalse());
//
//
//        long total = countQuery.fetchOne();
//
//        // PageImpl로 결과 반환
//        return new PageImpl<>(content, pageable, total);
//    }

    // 공지글 + 일반글 순서 페이징 처리 (중복 X) , 공지글 최대 3개까지
    @Transactional(readOnly = true)
    public Page<BoardPageResponse> findBoardsWithNoticeFirst(Pageable pageable) {
        QBoard board = QBoard.board;
        QCategory category = QCategory.category;
        QUser user = QUser.user;
        QImage image = QImage.image;

        List<BoardPageResponse> result = new ArrayList<>();


        int pageNumber = pageable.getPageNumber(); // 현재페이지 0부터시작
        int pageSize = pageable.getPageSize(); // 컨트롤러에서 지정한 PageSize = 8 기준

        // 1. 전체 공지글 수, 일반글 수 조회
        long totalNotice = queryFactory
                .select(board.count())
                .from(board)
                .where(board.isNotice.isTrue())
                .fetchOne();

        long totalCommon = queryFactory
                .select(board.count())
                .from(board)
                .where(board.isNotice.isFalse())
                .fetchOne();


        // 첫 페이지일떄만 공지글 + 일반글
        if (pageNumber == 0) {

            // 공지글 최대 3개 조회
            List<BoardPageResponse> noticeContent = queryFactory
                    .select(new QBoardPageResponse(
                            board.id,
                            user.id,
                            board.title,
                            board.content,
                            category.categoryName,
                            board.isNotice,
                            board.commentCount,
                            board.viewCount,
                            board.likeCount,
                            board.createdAt,
                            JPAExpressions.selectOne()
                                    .from(image)
                                    .where(image.board.id.eq(board.id))
                                    .exists()
                    ))
                    .from(board)
                    .leftJoin(board.user, user)
                    .leftJoin(board.category, category)
                    .where(board.isNotice.isTrue())
                    .orderBy(board.createdAt.desc())
                    .limit(3)
                    .fetch();

            int noticeSize = noticeContent.size();
            int commonLimit = pageSize - noticeSize; // 전체 게시글 - 공지글 개수 >> 일반게시글 가져오기

            // 일반글 offset 0 (첫 페이지)
            List<BoardPageResponse> commonContent = queryFactory
                    .select(new QBoardPageResponse(
                            board.id,
                            user.id,
                            board.title,
                            board.content,
                            category.categoryName,
                            board.isNotice,
                            board.commentCount,
                            board.viewCount,
                            board.likeCount,
                            board.createdAt,
                            JPAExpressions.selectOne()
                                    .from(image)
                                    .where(image.board.id.eq(board.id))
                                    .exists()
                    ))
                    .from(board)
                    .leftJoin(board.user, user)
                    .leftJoin(board.category, category)
                    .where(board.isNotice.isFalse())
                    .orderBy(board.createdAt.desc())
                    .offset(0)
                    .limit(commonLimit) // 공지글 가져온만큼 뺴서 가져온다
                    .fetch();

            result.addAll(noticeContent);
            result.addAll(commonContent);

            // 전체 게시글 수는 공지글 + 일반글 합
            long totalElements = totalNotice + totalCommon;

            return new PageImpl<>(result, pageable, totalElements);

        } else {


            // 공지글 개수 1페이지에 조회한 만큼
            int noticeSizeInFirstPage = (int) Math.min(totalNotice, 3);

            long offset = (long)(pageNumber - 1) * pageSize + (pageSize - noticeSizeInFirstPage);

            List<BoardPageResponse> commonContent = queryFactory
                    .select(new QBoardPageResponse(
                            board.id,
                            user.id,
                            board.title,
                            board.content,
                            category.categoryName,
                            board.isNotice,
                            board.commentCount,
                            board.viewCount,
                            board.likeCount,
                            board.createdAt,
                            JPAExpressions.selectOne()
                                    .from(image)
                                    .where(image.board.id.eq(board.id))
                                    .exists()
                    ))
                    .from(board)
                    .leftJoin(board.user, user)
                    .leftJoin(board.category, category)
                    .where(board.isNotice.isFalse())
                    .orderBy(board.createdAt.desc())
                    .offset(offset)
                    .limit(pageSize)
                    .fetch();


            return new PageImpl<>(commonContent, pageable, totalCommon + totalNotice);
        }
    }



}


