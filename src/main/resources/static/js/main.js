// 전역 변수 - 현재 페이지 상태
let currentPage = 0;

// 알림 개수 갱신 함수
function updateNotificationCount() {
  $.ajax({
    url: '/api/notification/unread',
    method: 'GET',
    success: function(count) {
      const $btn = $('#notificationBtn');
      const existingBadge = $btn.find('.badge');

      if (count > 0) {
        if (existingBadge.length) {
          existingBadge.text(count);
        } else {
          $btn.append(`
            <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger">
              ${count}
              <span class="visually-hidden">unread messages</span>
            </span>
          `);
        }
      } else {
        existingBadge.remove();
      }
    },
    error: function() {
      console.error('알림 개수 조회 실패');
    }
  });
}

// 알림 목록 로드 함수 (페이징 포함)
function loadNotifications(page = 0) {
  currentPage = page;

  $.ajax({
    url: '/api/notification',
    method: 'GET',
    data: { page: page },
    success: function(pageData) {
      let html = '';

      pageData.content.forEach(function(notification) {
        const readClass = notification.read ? 'text-muted' : '';

        html += `
          <div class="border-bottom py-2 d-flex justify-content-between align-items-center" data-id="${notification.id}">
            <div class="notification-message ${readClass}" style="cursor:pointer;">
              ${notification.message}
              <small class="text-muted ms-2">${notification.formattedCreatedAt}</small>
            </div>
            <div>
              <button class="btn btn-danger btn-xs delete-btn ms-2" aria-label="삭제" title="삭제" style="padding: 0 6px; font-size: 0.75rem; line-height: 1;">×</button>



            </div>
          </div>
        `;
      });

      if (html === '') {
        html = '<p>알림이 없습니다.</p>';
      }

      $('#notificationList').html(html);

      renderPagination(pageData);

      // 읽음 처리 - 메시지 클릭 이벤트
      $('.notification-message').click(function() {
        const $this = $(this);
        const id = $this.closest('div[data-id]').data('id');

        if ($this.hasClass('text-muted')) return;

        $.ajax({
          url: `/api/notification/${id}/read`,
          method: 'PUT',
          success: function() {
            updateNotificationCount();
            $this.addClass('text-muted');
          },
          error: function() {
            alert('읽음 처리 실패');
          }
        });
      });

      // 삭제 버튼 이벤트 등록
      $('.delete-btn').click(function() {
        const id = $(this).closest('div[data-id]').data('id');
        deleteNotification(id);
      });
    },
    error: function() {
      $('#notificationList').html('<p>알림을 불러오는데 실패했습니다.</p>');
    }
  });
}

// 읽음 처리 함수
function markAsRead(notificationId) {
  $.ajax({
    url: `/api/notification/${notificationId}/read`,
    method: 'PUT',
    success: function() {
      updateNotificationCount();
      loadNotifications(currentPage);
    },
    error: function() {
      alert('읽음 처리 실패');
    }
  });
}

// 삭제 처리 함수
function deleteNotification(notificationId) {
  $.ajax({
    url: `/api/notification/${notificationId}`,
    method: 'DELETE',
    success: function() {
      updateNotificationCount();
      loadNotifications(currentPage);
    },
    error: function() {
      alert('삭제 실패');
    }
  });
}

// 페이징 UI 렌더링 함수
function renderPagination(page) {
  let paginationHtml = '<nav aria-label="Page navigation"><ul class="pagination justify-content-center my-2">';

  if (page.hasPrevious) {
    paginationHtml += `<li class="page-item"><a class="page-link" href="#" data-page="${page.number - 1}">Previous</a></li>`;
  } else {
    paginationHtml += `<li class="page-item disabled"><span class="page-link">Previous</span></li>`;
  }

  for (let i = 0; i < page.totalPages; i++) {
    paginationHtml += `<li class="page-item ${i === page.number ? 'active' : ''}">
      <a class="page-link" href="#" data-page="${i}">${i + 1}</a>
    </li>`;
  }

  if (page.hasNext) {
    paginationHtml += `<li class="page-item"><a class="page-link" href="#" data-page="${page.number + 1}">Next</a></li>`;
  } else {
    paginationHtml += `<li class="page-item disabled"><span class="page-link">Next</span></li>`;
  }

  paginationHtml += '</ul></nav>';

  $('#notificationList').append(paginationHtml);

  // 페이지 링크 클릭 이벤트 등록
  $('#notificationList nav .page-link').click(function(e) {
    e.preventDefault();
    const targetPage = $(this).data('page');
    if (targetPage !== undefined) {
      loadNotifications(targetPage);
    }
  });
}

// 문서 준비 완료 시 초기화
$(document).ready(function() {
  updateNotificationCount();
  setInterval(updateNotificationCount, 5000);

  // 알림 버튼 클릭 토글
  $('#notificationBtn').click(function() {
    const $container = $('#notificationListContainer');

    if ($container.is(':visible')) {
      $container.hide();
    } else {
      $container.show();
      loadNotifications(0);
    }
  });

  // 닫기 버튼 클릭 시 숨기기
  $('#closeNotificationList').click(function() {
    $('#notificationListContainer').hide();
  });
});
