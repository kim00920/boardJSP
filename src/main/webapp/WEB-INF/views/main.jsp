<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8" />
    <title>게시글 목록</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" />
    <link href="/css/main.css" rel="stylesheet" />
</head>

<body class="bg-light">
<div class="container" style="max-width: 1200px; margin-top: 50px;">
    <div class="border border-dark rounded p-4 bg-white shadow">

        <!-- 제목, 검색창 -->
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2 class="mb-0">
                <a href="/main" style="text-decoration: none; color: inherit;">게시글 목록</a>
            </h2>
            <form class="d-flex" action="/search" method="get" style="max-width: 300px;">
                <input class="form-control form-control-sm" type="search" name="keyword" placeholder="검색어를 입력하세요" aria-label="검색" />
                <button class="btn btn-outline-primary btn-sm ms-2" type="submit">검색</button>
            </form>
        </div>


        <table class="table table-hover table-bordered align-middle mb-4">
            <thead class="table-dark">
                <tr>
                    <th>공지 여부</th>
                    <th>게시글 번호</th>
                    <th>카테고리</th>
                    <th>제목</th>
                    <th>조회수</th>
                    <th>좋아요</th>
                    <th>작성일</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="board" items="${boards.content}">
                    <tr class="${board.notice ? 'table-secondary' : ''}">
                        <td>
                            <c:choose>
                                <c:when test="${board.notice}">
                                    <strong>공지</strong>
                                </c:when>
                                <c:otherwise>일반</c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <a href="/board/${board.id}">${board.id}</a>
                            <span class="text-muted">[${board.commentCount}]</span>
                            <c:if test="${board.existImage}">
                                <span title="이미지 포함" style="margin-left: 5px;">📷</span>
                            </c:if>
                        </td>
                        <td>${board.categoryName}</td>
                        <td>${board.title}</td>
                        <td>${board.viewCount}</td>
                        <td>${board.likeCount}</td>
                        <td>${board.formattedCreatedAt}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <!-- 정렬 + 작성 버튼 -->
        <div class="d-flex justify-content-between align-items-center">
            <div class="btn-group" role="group">
                <a href="/sorted/VIEW_DESC" class="btn btn-outline-secondary btn-sm">조회수 많은 순</a>
                <a href="/sorted/VIEW_ASC" class="btn btn-outline-secondary btn-sm">조회수 적은 순</a>
                <a href="/sorted/LIKE_DESC" class="btn btn-outline-danger btn-sm">좋아요 많은 순</a>
                <a href="/sorted/LIKE_ASC" class="btn btn-outline-danger btn-sm">좋아요 적은 순</a>
            </div>
            <a href="/boardForm" class="btn btn-primary btn-sm">게시글 작성하기</a>
        </div>

    <!-- 페이징 -->
    <c:if test="${boards.totalPages > 0}">
        <nav class="mt-4" aria-label="Page navigation">
            <ul class="pagination justify-content-center">
                <c:if test="${boards.hasPrevious()}">
                    <li class="page-item">
                        <c:choose>
                            <c:when test="${not empty keyword}">
                                <a class="page-link" href="?page=${boards.number - 1}&size=${boards.size}&keyword=${fn:escapeXml(keyword)}">Previous</a>
                            </c:when>
                            <c:otherwise>
                                <a class="page-link" href="?page=${boards.number - 1}&size=${boards.size}">Previous</a>
                            </c:otherwise>
                        </c:choose>
                    </li>
                </c:if>
                <c:forEach begin="0" end="${boards.totalPages - 1}" var="pageNum">
                    <li class="page-item ${pageNum == boards.number ? 'active' : ''}">
                        <c:choose>
                            <c:when test="${not empty keyword}">
                                <a class="page-link" href="?page=${pageNum}&size=${boards.size}&keyword=${fn:escapeXml(keyword)}">${pageNum + 1}</a>
                            </c:when>
                            <c:otherwise>
                                <a class="page-link" href="?page=${pageNum}&size=${boards.size}">${pageNum + 1}</a>
                            </c:otherwise>
                        </c:choose>
                    </li>
                </c:forEach>
                <c:if test="${boards.hasNext()}">
                    <li class="page-item">
                        <c:choose>
                            <c:when test="${not empty keyword}">
                                <a class="page-link" href="?page=${boards.number + 1}&size=${boards.size}&keyword=${fn:escapeXml(keyword)}">Next</a>
                            </c:when>
                            <c:otherwise>
                                <a class="page-link" href="?page=${boards.number + 1}&size=${boards.size}">Next</a>
                            </c:otherwise>
                        </c:choose>
                    </li>
                </c:if>
            </ul>
        </nav>
    </c:if>
</div>
</div>



<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
