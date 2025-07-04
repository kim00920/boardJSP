<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8" />
    <title>회원가입</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" />
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body class="bg-light">

<div class="container border border-dark rounded p-4 bg-white" style="max-width: 500px; margin-top: 80px;">
    <h2 class="mb-4 text-center">회원가입</h2>

    <form id="signupForm">

        <div class="mb-3">
            <label for="loginId" class="form-label">로그인 아이디</label>
            <input type="text" class="form-control" id="loginId" name="loginId" placeholder="아이디를 입력하세요" required />
            <div id="loginIdFeedback" class="invalid-feedback"></div>
        </div>

        <div class="mb-3">
            <label for="password" class="form-label">비밀번호</label>
            <input type="password" class="form-control" id="password" name="password" placeholder="비밀번호를 입력하세요" required />
        </div>

        <div class="mb-3">
            <label for="name" class="form-label">이름</label>
            <input type="text" class="form-control" id="name" name="name" placeholder="이름을 입력하세요" required />
        </div>

        <div class="mb-3">
            <label for="email" class="form-label">이메일</label>
            <input type="email" class="form-control" id="email" name="email" placeholder="example@domain.com" required />
        </div>

        <button type="submit" class="btn btn-success w-100">회원가입</button>
    </form>

    <div class="mt-3 text-center">
        <a href="/login">이미 계정이 있으신가요? 로그인</a>
    </div>
</div>

<script src="/js/signup.js"></script>

</body>
</html>
