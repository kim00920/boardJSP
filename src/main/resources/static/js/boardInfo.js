let currentPage = 0; // 0부터 시작하는 현재 페이지 전역변수

// 댓글 개수 + 댓글 목록 전체 동적 업데이트 (page 기본값 0)
function loadComments(page = 0) {
  currentPage = page; // 현재 페이지 상태 저장

  $.get(`/board/${boardId}/comments?page=${page}`)
    .done(function (response) {
      renderComments(response.content); // 댓글 목록 렌더링
      updateCommentCount(response.totalCount); // 댓글 + 대댓글 개수 업데이트
      renderPagination(response.currentPage, response.totalPages, response.hasNext, response.hasPrevious); // 페이징 버튼 렌더링
    })
    .fail(() => alert('댓글 목록을 불러오는 데 실패했습니다.'));
}

// 댓글 개수 업데이트
function updateCommentCount(count) {
  $('.comments-section > h4').html(`댓글 목록 (${count})`);
}

// 댓글 + 대댓글 렌더링
function renderComments(comments) {
  const container = $('.comments-section');
  container.find('p').remove();
  container.find('.card.mb-2').remove();

  if (comments.length === 0) {
    container.append('<p>댓글이 없습니다.</p>');
    return;
  }

  comments.forEach(comment => {
    let repliesHtml = '';
    if (comment.replyResponse && comment.replyResponse.length > 0) {
      repliesHtml += '<div class="ms-4 mt-3"><strong>대댓글:</strong>';
      comment.replyResponse.forEach(reply => {
        repliesHtml += `
          <div class="card mt-2">
            <div class="card-body">
              <div class="d-flex justify-content-between">
               <p class="mb-0"><strong>${reply.userName}</strong></p>
               <p class="mb-0"><strong>${reply.createdAtStr}</strong></p>
              </div>
              <p>${reply.replyComment}</p>
              <div class="mt-2">
                <button class="btn btn-sm btn-outline-primary me-1 reply-edit-btn" data-reply-id="${reply.id}">대댓글수정</button>
                <button class="btn btn-sm btn-outline-danger reply-delete-btn" data-reply-id="${reply.id}">대댓글삭제</button>
              </div>
            </div>
          </div>
        `;
      });
      repliesHtml += '</div>';
    }

    const commentHtml = `
      <div class="card mb-2">
        <div class="card-body">
          <div class="d-flex justify-content-between">
            <p class="mb-0"><strong>${comment.userName}</strong></p>
            <p class="mb-0"><strong>${comment.createdAtStr}</strong></p>
          </div>
          <p>${comment.comment}</p>
          <div class="mt-2">
            <button class="btn btn-sm btn-outline-primary me-1 comment-edit-btn" data-comment-id="${comment.id}">댓글수정</button>
            <button class="btn btn-sm btn-outline-danger comment-delete-btn" data-comment-id="${comment.id}">댓글삭제</button>
          </div>
          ${repliesHtml}
          <button class="btn btn-sm btn-outline-secondary mt-3 toggle-reply-btn" type="button" data-comment-id="${comment.id}">
            대댓글 작성하기
          </button>
          <div id="replyForm-${comment.id}" class="mt-2 reply-form" style="display:none;">
            <form action="/comment/${comment.id}/replyComment" method="post">
              <div class="mb-3">
                <textarea name="replyComment" class="form-control" rows="2" placeholder="대댓글은 최대 100자 까지 가능합니다" maxlength="100" required></textarea>
              </div>
              <button type="submit" class="btn btn-primary btn-sm">대댓글 등록하기</button>
            </form>
          </div>
        </div>
      </div>
    `;
    container.append(commentHtml);
  });
}

// 버튼 페이징
function renderPagination(currentPage, totalPages, hasNext, hasPrevious) {
  const $pagination = $('#pagination');
  $pagination.empty();

  if (totalPages <= 1) return; // 페이지가 1개 이하일 땐 숨김

  // 이전 버튼
  if (hasPrevious) {
    $pagination.append(`<button class="btn btn-outline-primary btn-sm me-1 page-btn" data-page="${currentPage - 1}">&laquo; 이전</button>`);
  }

  // 숫자 페이지 버튼
  for (let i = 0; i < totalPages; i++) {
    const activeClass = i === currentPage ? 'btn-primary' : 'btn-outline-primary';
    $pagination.append(`
      <button class="btn ${activeClass} btn-sm me-1 page-btn" data-page="${i}">${i + 1}</button>
    `);
  }

  // 다음 버튼
  if (hasNext) {
    $pagination.append(`<button class="btn btn-outline-primary btn-sm page-btn" data-page="${currentPage + 1}">다음 &raquo;</button>`);
  }
}

// jQuery 문서 준비 후 이벤트 바인딩
$(document).ready(function () {
  // 페이지 버튼 클릭
  $(document).on('click', '.page-btn', function () {
    const page = $(this).data('page');
    loadComments(page);
  });

  // 댓글 등록
  $('form[action^="/board/"][action$="/comment"]').on('submit', function (e) {
    e.preventDefault();
    const form = $(this);
    const url = form.attr('action');
    const data = form.serialize();

    $.post(url, data)
      .done(() => {
        form[0].reset();
        loadComments(currentPage); // 현재 페이지 유지하며 새로고침
      })
      .fail(() => alert('댓글 등록에 실패했습니다.'));
  });

  // 대댓글 등록
  $(document).on('submit', 'form[action^="/comment/"][action$="/replyComment"]', function (e) {
    e.preventDefault();
    const form = $(this);
    const url = form.attr('action');
    const data = form.serialize();

    $.post(url, data)
      .done(() => {
        form[0].reset();
        form.closest('.reply-form').hide();
        loadComments(currentPage); // 현재 페이지 유지하며 새로고침
      })
      .fail(() => alert('대댓글 등록에 실패했습니다.'));
  });

  // 대댓글 작성 폼 토글
  $(document).on('click', '.toggle-reply-btn', function () {
    const commentId = $(this).data('comment-id');
    $('#replyForm-' + commentId).toggle();
  });

  // 댓글 삭제
  $(document).on('click', '.comment-delete-btn', function () {
    const commentId = $(this).data('comment-id');
    if (confirm('댓글을 삭제하시겠습니까?')) {
      $.ajax({
        url: `/comment/${commentId}`,
        type: 'DELETE',
        success: () => {
          alert('댓글이 삭제되었습니다.');
          loadComments(currentPage);
        },
        error: () => alert('댓글 삭제에 실패했습니다.')
      });
    }
  });

  // 댓글 수정 토글 (수정폼 보이기)
  $(document).on('click', '.comment-edit-btn', function () {
    const $card = $(this).closest('.card-body');
    const commentId = $(this).data('comment-id');
    const $commentText = $card.find('p').eq(2);
    const originalText = $commentText.text().trim();

    $commentText.replaceWith(`
      <textarea class="form-control edit-comment-textarea" rows="3">${originalText}</textarea>
      <button class="btn btn-sm btn-success mt-2 save-comment-btn" data-comment-id="${commentId}">저장</button>
      <button class="btn btn-sm btn-secondary mt-2 cancel-edit-btn">취소</button>
    `);
  });

  // 댓글 수정 저장
  $(document).on('click', '.save-comment-btn', function () {
    const commentId = $(this).data('comment-id');
    const newComment = $(this).siblings('.edit-comment-textarea').val();

    $.ajax({
      url: `/comment/${commentId}`,
      type: 'PUT',
      contentType: 'application/json',
      data: JSON.stringify({ comment: newComment }),
      success: () => {
        alert('댓글이 수정되었습니다.');
        loadComments(currentPage);
      },
      error: () => alert('댓글 수정에 실패했습니다.')
    });
  });

  // 댓글 수정 취소
  $(document).on('click', '.cancel-edit-btn', function () {
    loadComments(currentPage);
  });

  // 게시글 삭제
  $('#deleteBtn').click(function () {
    if (!confirm('정말 삭제하시겠습니까?')) return;

    const boardId = $(this).data('board-id');
    $.ajax({
      url: `/board/${boardId}`,
      type: 'DELETE',
      success: () => {
        alert('게시글이 삭제되었습니다.');
        window.location.href = '/main';
      },
      error: () => alert('작성자만 가능합니다')
    });
  });

  // 게시글 좋아요 상태 확인용
  const boardId = $('#likeBtn').data('board-id');
  $.get(`/board/${boardId}/like/status`, function (res) {
    if (res.liked) {
      $('#likeBtn').addClass('liked btn-danger').removeClass('btn-outline-danger');
    } else {
      $('#likeBtn').removeClass('liked btn-danger').addClass('btn-outline-danger');
    }
  });

  // 좋아요 목록 갱신 함수 (좋아요 목록이 켜져 있을 때만 새로고침)
  function updateLikeUserList() {
    const $listDiv = $('#likeUserList');
    const $list = $listDiv.find('ul');

    if ($listDiv.is(':visible')) {
      $.ajax({
        url: `/board/${boardId}/like/list`,
        type: 'GET',
        dataType: 'json',
        success: function (data) {
          $list.empty();
          if (data.length === 0) {
            $list.append('<li class="list-group-item">좋아요를 누른 사람이 없습니다.</li>');
          } else {
            data.forEach(user => {
              $list.append(`<li class="list-group-item">${user.userLoginId} (${user.userName})</li>`);
            });
          }
        },
        error: function () {
          alert('좋아요 목록을 불러오는 데 실패했습니다.');
        }
      });
    }
  }

  // 게시글 좋아요 또는 취소하기
  $('#likeBtn').click(function () {
    const isLiked = $(this).hasClass('liked');

    const updateLikeCount = () => {
      $.get(`/board/${boardId}/like/count`, function (res) {
        $('#likeCount').text(res.count);
      });
    };

    if (!isLiked) {
      $.ajax({
        url: `/board/${boardId}/like`,
        method: 'POST',
        success: () => {
          $('#likeBtn').addClass('liked btn-danger').removeClass('btn-outline-danger');
          updateLikeCount();
          updateLikeUserList();
        }
      });
    } else {
      $.ajax({
        url: `/board/${boardId}/like`,
        method: 'DELETE',
        success: () => {
          $('#likeBtn').removeClass('liked btn-danger').addClass('btn-outline-danger');
          updateLikeCount();
          updateLikeUserList();
        }
      });
    }
  });

  // 대댓글 삭제
  $(document).on('click', '.reply-delete-btn', function () {
    const replyId = $(this).data('reply-id');

    if (confirm('대댓글을 삭제하시겠습니까?')) {
      $.ajax({
        url: `/replyComment/${replyId}`,
        type: 'DELETE',
        success: () => {
          alert('대댓글이 삭제되었습니다.');
          loadComments(currentPage);
        },
        error: () => alert('대댓글 삭제에 실패했습니다.')
      });
    }
  });

  // 대댓글 수정 토글 (수정폼 보이기)
  $(document).on('click', '.reply-edit-btn', function () {
    const $card = $(this).closest('.card-body');
    const replyId = $(this).data('reply-id');
    const $replyText = $card.find('p').eq(2);
    const originalText = $replyText.text().trim();

    $replyText.replaceWith(`
      <textarea class="form-control edit-reply-textarea" rows="3">${originalText}</textarea>
      <button class="btn btn-sm btn-success mt-2 save-reply-btn" data-reply-id="${replyId}">저장</button>
      <button class="btn btn-sm btn-secondary mt-2 cancel-reply-edit-btn">취소</button>
    `);
  });

  // 대댓글 수정 저장
  $(document).on('click', '.save-reply-btn', function () {
    const replyId = $(this).data('reply-id');
    const newReply = $(this).siblings('.edit-reply-textarea').val();

    $.ajax({
      url: `/replyComment/${replyId}`,
      type: 'PUT',
      contentType: 'application/json',
      data: JSON.stringify({ replyComment: newReply }),
      success: () => {
        alert('대댓글이 수정되었습니다.');
        loadComments(currentPage);
      },
      error: () => alert('대댓글 수정에 실패했습니다.')
    });
  });

  // 대댓글 수정 취소
  $(document).on('click', '.cancel-reply-edit-btn', function () {
    loadComments(currentPage);
  });

  // 좋아요 목록 토글 및 로드
  $('#likeListBtn').click(function () {
    const $listDiv = $('#likeUserList');
    const $list = $listDiv.find('ul');

    if ($listDiv.is(':visible')) {
      $listDiv.hide();
    } else {
      $.ajax({
        url: `/board/${boardId}/like/list`,
        type: 'GET',
        dataType: 'json',
        success: function (data) {
          $list.empty();
          if (data.length === 0) {
            $list.append('<li class="list-group-item">좋아요를 누른 사람이 없습니다.</li>');
          } else {
            data.forEach(user => {
              $list.append(`<li class="list-group-item">${user.userLoginId} (${user.userName})</li>`);
            });
          }
          $listDiv.show();
        },
        error: function () {
          alert('좋아요 목록을 불러오는 데 실패했습니다.');
        }
      });
    }
  });

      // 공지 상태 초기 확인
      $('#noticeBtn').data('board-id');
      $.get(`/board/${boardId}/notice/status`, function (res) {
      const isNotice = res.notice;
      const $btn = $('#noticeBtn');

      if (isNotice) {
        $btn.text('공지 해제').removeClass('btn-outline-warning').addClass('btn-warning');
      } else {
        $btn.text('공지 등록').removeClass('btn-warning').addClass('btn-outline-warning');
      }
    });

    $('#noticeBtn').click(function () {
      const $btn = $(this);
      const boardId = $btn.data('board-id');

      $.ajax({
        url: `/board/${boardId}/setNotice`,
        type: 'PUT',
        success: () => {

          if ($btn.hasClass('btn-outline-warning')) {
                  $btn.text('공지 해제').removeClass('btn-outline-warning').addClass('btn-warning');
                } else {
                  $btn.text('공지 등록').removeClass('btn-warning').addClass('btn-outline-warning');
                }

          // 상태 다시 확인
          $.get(`/board/${boardId}/notice/status`, function (res) {
            const isNotice = res.notice;

            if (isNotice) {
              $btn.text('공지 해제').removeClass('btn-outline-warning').addClass('btn-warning');
            } else {
              $btn.text('공지 등록').removeClass('btn-warning').addClass('btn-outline-warning');
            }
          });
        },
        error: () => alert('공지 상태 변경에 실패했습니다.')
      });
    });




  // 초기 댓글 + 대댓글 로드
  loadComments(currentPage);
});
