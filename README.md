# 프로젝트 개요
기존에 만든 게시글 REST API 에 jsp, javascript, bootstrap 을 통해 만들었습니다<br>
https://github.com/kim00920/board<br>
개발 기간 : 25.06.30 ~ 25.07.05<br>
참여 인원 : 1명<br>

# 기술 스택
Language : Java<br>
JDK : 17<br>
Framework : Spring 6.2.5 , SpringBoot 3.4.4<br>
Library : Spring Security 6.4.4, Query DSL 5.1.0<br>
BuildTool : Gradle<br>
DB : MySQL<br>
File : AWS S3<br><br>
message Queue : rabbitMQ<br>

Front : Jsp, JavaScript, Bootstrap<br>








# API 실행 흐름

1. 회원<br>


1.1 회원가입 - 로그인아이디 중복 유효성 검사 및 회원가입<br>
1.2 로그인 - Security 세션 방식으로 로그인 후 메인 페이지 이동<br>
1.3 비밀번호 변경<br>

![Image](https://github.com/user-attachments/assets/7c6a4dfa-b711-4a03-90a6-ef2688428f42)<br>
   

2. 게시글 <br>
- 공지글은 맨위에 항상 보여주며(최대 3개), 이떄 페이징은 공지글을 제외한 일반글에서만 페이징을 처리한다 
- 게시글 번호 옆에는 게시글의 총 댓글수 (댓글 + 대댓글) 가  [] 에 표시되며, 이떄 게시글의 이미지가 1개라도 존재할떄 (board.getImages() == true), 이미지 사진이 표시된다<br><br>

![Image](https://github.com/user-attachments/assets/caaca2f3-dd08-499d-883a-6f5784ea30ee)<br><br>
2.1 등록 - 제목, 내용, 카테고리, 이미지를 넣어서 게시글을 생성, 이떄 카테고리는 테이블에 존재하는 카테고리만을 넣을수있고, 이미지는 S3를 사용해 최소 0개 ~ 2개 이상 등록가능<br>
![Image](https://github.com/user-attachments/assets/57dfccb8-6a10-469c-a899-3736face760a)<br><br>

2.2 수정 - 작성자만 수정 가능<br>
![Image](https://github.com/user-attachments/assets/8d0cff9f-e7fd-4815-9362-326e30b6ad6f)<br><br>

2.3 삭제 - 작성자 또는 관리자만이 삭제 가능<br><br>

2.4 좋아요 및 취소하기 및 좋아요 누른 회원 조회 - ajax 를 통해서 게시글 좋아요 수가 늘어나거나 감소하고, 좋아요를 누른 회원 목록도 즉시 동적 업데이트된다<br>
![Image](https://github.com/user-attachments/assets/ac3b8212-82e6-4b73-bafa-0383d4ae4925)<br><br>

2.4 정렬조회 - 게시글의 좋아요 수나 조회수 개수가 적거나 많은 순서로 정렬조회<br>
![Image](https://github.com/user-attachments/assets/c457d11e-2ac1-4435-81c0-952657ac8a74)<br><br>

2.5 검색조회 - 게시글 제목에 대해서 검색하여 정렬조회<br>
![Image](https://github.com/user-attachments/assets/ef974ec9-4f5d-4477-a415-452c25686274)<br><br>


3. 댓글<br>
- ajax 를 통해서 댓글 등록, 수정, 삭제를 할수있으며, 새로고침하지않고 일정 부분만 동적 업데이트를 한다<br>


3.1 등록<br>
3.2 수정 - 작성자만 수정 가능<br>
3.3 삭제 - 작성자만 삭제 가능<br>

![Image](https://github.com/user-attachments/assets/e6351926-1ef3-4970-a911-210dc6809417)<br><br>
   
4. 대댓글<br>
- ajax 를 통해서 대댓글 등록, 수정, 삭제를 할수있으며, 새로고침하지않고 일정 부분만 동적 업데이트를 한다<br>


4.1 등록<br>
4.2 수정 - 작성자만 수정 가능<br>
4.3 삭제 - 작성자만 삭제 가능<br>

![Image](https://github.com/user-attachments/assets/00722a48-c09c-4ac7-bb9b-f382bdeacb0c)<br><br>

# 느낀점 및 개선점
- 게시글을 조회할떄 jsp 로 렌더링 되는 폼 영역과 js 로 렌더링되는 영역이 일치하지 않아서 새로고침 될떄마다 UI에 위화감이 들었다<br>
  → 게시글 정보 와 댓글 폼 부분까지만 jsp 로 작성하고, 댓글 목록 (댓글 + 대댓글) 란은 js 를 통해서 동적 렌더링으로 리팩토링했다<br><br>

- 댓글 또는 대댓글을 수정하거나 삭제하면 회원이 현재 실행한 페이지가 아닌 1번 페이징으로 돌아가게되는 문제가 발생<br>
  → 기존에는 댓글, 대댓글을 수정하거나 삭제하고 난 이후 콜백함수를 loadComments() 를 통해 렌더링을 했지만, 현재 페이징을 저장하는 전역변수 currentPage = 0; 을 선언해서 <br>
  loadComments(currentPage) 콜백함수로 리팩토링을 통해 댓글을 수정하거나 삭제해도 현재 댓글 페이징을 유지하게 했다<br>
