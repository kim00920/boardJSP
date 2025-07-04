<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8" />
    <title>게시글 상세 정보</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" />
</head>
<body class="bg-light">
<div class="container mt-5">

  <div class="d-flex gap-3">

    <div class="card mb-4 flex-grow-1">
        <div class="card-header">
            <strong>${board.title}</strong>

            <div class="d-flex justify-content-between align-items-center mb-2">
                <div class="d-flex align-items-center flex-wrap">
                    <p class="mb-0 me-3"><strong>조회수:</strong> ${board.viewCount}</p>
                    <p class="mb-0 me-3"><strong>좋아요:</strong> <span id="likeCount">${board.likeCount}</span></p>
                    <p class="mb-0 me-3"><strong>카테고리:</strong> ${board.categoryName}</p>
                    <p class="mb-0"><strong>작성일:</strong> ${createdAtDate}</p>
                </div>

               <!-- 게시글 버튼 -->
               <div class="d-flex align-items-center">
                   <button id="likeBtn" data-board-id="${board.id}" class="btn btn-sm btn-danger me-2">좋아요</button>

                  <button id="noticeBtn" data-board-id="${board.id}" class="btn btn-sm ${board.notice ? 'btn-warning' : 'btn-outline-warning'}">
                    ${board.notice ? '공지 해제' : '공지 등록'} </button>
                   <a href="/board/${board.id}/updateForm" class="btn btn-sm btn-outline-primary me-2">수정</a>
                   <button id="deleteBtn" data-board-id="${board.id}" class="btn btn-sm btn-outline-danger me-2">삭제</button>
                   <button id="likeListBtn" data-board-id="${board.id}" class="btn btn-sm btn-outline-info">좋아요 목록</button>
               </div>
            </div>
        </div>

        <div class="card-body">
            <p><strong>작성자 :</strong> ${board.userName}</p>
            <hr/>
            <p><strong>${board.content}</strong></p>

            <c:if test="${not empty board.imageList}">
                <div class="mb-3">
                    <div class="d-flex flex-wrap gap-3 mt-2">
                        <c:forEach var="image" items="${board.imageList}">
                            <img src="${image.fileUrl}" alt="Image" class="img-thumbnail" style="width: 200px; height: 200px; object-fit: cover;" />
                        </c:forEach>
                    </div>
                </div>
            </c:if>
        </div>
    </div>

    <div id="likeUserList" class="card" style="width: 300px; display: none; max-height: 400px; overflow-y: auto;">
      <h6 class="card-header"><strong>좋아요 누른 회원 목록</strong></h6>
      <ul class="list-group list-group-flush mb-0"></ul>
    </div>
  </div>


 <div class="d-flex justify-content-start mb-3">
     <a href="/main" class="btn btn-secondary btn-sm">목록</a>
 </div>

  <div class="card mb-4 mt-4">
      <div class="card-body">
          <form action="/board/${board.id}/comment" method="post">
              <div class="mb-3">
                  <label for="comment" class="form-label"><strong>댓글 입력</strong></label>
                  <textarea id="comment" name="comment" class="form-control" rows="3" placeholder="댓글은 최대 100자 까지 가능합니다" maxlength="100" required></textarea>
              </div>
              <button type="submit" class="btn btn-primary">댓글 등록하기</button>
          </form>
      </div>
  </div>

  <div class="comments-section">
    <h4>댓글 목록 (<span id="commentCount">${board.commentCount}</span>)</h4>
    <div id="pagination" class="mt-3"></div> <!-- 버튼 페이징 버튼 -->
  </div>

</div>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

<script>
  const boardId = "${board.id}";
</script>

<script src="/js/boardInfo.js"></script>

</body>
</html>
