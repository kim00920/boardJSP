<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8" />
    <title>로그인</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" />
</head>
<body class="bg-light">
<div class="container border border-dark rounded p-4 bg-white" style="max-width: 500px; margin-top: 80px;">
    <h2 class="mb-4 text-center">로그인</h2>
    <form action="/loginForm" method="post">
        <div class="mb-3">
            <label for="loginId" class="form-label">로그인 아이디</label>
            <input type="text" class="form-control" id="loginId" name="loginId" placeholder="로그인 아이디를 입력하세요" required />
        </div>
        <div class="mb-3">
            <label for="password" class="form-label">비밀번호</label>
            <input type="password" class="form-control" id="password" name="password" placeholder="비밀번호를 입력하세요" required />
        </div>
        <button type="submit" class="btn btn-primary w-100">로그인</button>
    </form>
</div>
</body>
</html>
