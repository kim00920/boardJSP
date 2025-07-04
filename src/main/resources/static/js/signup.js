$(document).ready(function () {
  let isLoginIdValid = false;

  // 로그인 아이디 중복 체크
  $('#loginId').on('blur', function () {
    const loginId = $(this).val().trim();
    if (loginId === '') return;

    $.ajax({
      type: 'POST',
      url: '/exist',
      data: { loginId: loginId },
      success: function () {
        // 사용 가능한 아이디
        $('#loginId').removeClass('is-invalid').addClass('is-valid');
        $('#loginIdFeedback').text('');
        isLoginIdValid = true;
      },
      error: function (xhr) {
        $('#loginId').removeClass('is-valid').addClass('is-invalid');
        $('#loginIdFeedback').text('이미 사용중인 아이디입니다.');
        isLoginIdValid = false;
      }
    });
  });

  // 회원가입 폼 제출
  $('#signupForm').on('submit', function (e) {
    e.preventDefault();

    if (!isLoginIdValid) {
      alert('아이디 중복 확인이 필요합니다.');
      $('#loginId').focus();
      return;
    }

    const data = {
      loginId: $('#loginId').val(),
      password: $('#password').val(),
      name: $('#name').val(),
      email: $('#email').val()
    };

    $.ajax({
      type: 'POST',
      url: '/signup',
      contentType: 'application/json',
      data: JSON.stringify(data),
      success: function () {
        alert('회원가입이 완료되었습니다.');
        location.href = '/';
      },
      error: function (xhr) {
        alert(xhr.responseText || '회원가입에 실패했습니다.');
      }
    });
  });
});
