# 구인구직 사이트 프로젝트 2차

## 시연 영상
[![2차_미니프로젝트_시연영상](http://img.youtube.com/vi/oGc5zsfmxl4/0.jpg)](https://youtu.be/oGc5zsfmxl4)

## 발표 PPT
![2차_미니프로젝트_발표PPT](https://github.com/K-Minho/mini-project2/blob/68b84eca6ec0f603d7b0c384462382432a99ae86/%EB%AF%B8%EB%8B%88%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8_2%EC%A1%B0.pdf)

## 팀 소개
- 김태훈(팀장)
- 이상현
- 강민호
- 김지윤

## 기술스택
![기술스택](https://user-images.githubusercontent.com/118786401/233411539-b1f8fc3a-9f97-427a-b30c-fc685018eed4.png)

## 기능
**자바라**는 구인하는 기업과 구직하는 구직자의 매칭 기능 제공하는 사이트입니다.

### 공통 기능
- 기업/일반 회원 로그인 시 하나의 메소드만 호출하는 방식으로 구현
- 회원가입 시 유저네임 중복 체크 및 비밀번호 유효성(동일한 비밀번호를 입력했는지) 체크
- 회원정보수정 시 버튼 한번 클릭으로 사진까지 수정
- 사진 등록 전 미리 보기 기능
- 레디스로 구현하여 브라우저가 닫혀도(JSessionID가 없어도) 로그인 유지
- 구직자/공고 페이징 기능

### 기업 관련 기능
- 이력서 등록한 원하는 기술을 보윻란 유저 추천 기능
- 구직자 목록 정렬 조건 기능
- 공고 등록 기능
- 등록한 공고 목록 수정 및 삭제 기능
- 지원한 구직자 합/불합격 선택 기능

### 구직자 관련 기능

- 채용 합/불합격시 등록된 이메일로 메일링 기능
- 여러개의 자소서 쓰기 및 수정 기능
- 공고 지원 기능
- 보유 기술을 등록하여 기업 추천받기 기능
- 관심 있는 기업 북마크 

## 주요 기능 시연
|||
|:--:|:--:|
|회원가입|로그인|
|![회원가입1](https://user-images.githubusercontent.com/118786401/232196774-ce324eb5-a4ad-4ccb-9625-7efee63358be.gif)|![로그인](https://user-images.githubusercontent.com/118786401/232635843-3aa73e3d-7f1d-496e-bf1a-eaa79a8b8829.gif)|
|정보 수정|공고 등록|
|![정보-수정](https://user-images.githubusercontent.com/118786401/232636464-ffc9660d-5867-4878-b1bb-5f19956329ef.gif)|![공고등록](https://user-images.githubusercontent.com/118786401/232636481-0759ccc8-5389-406d-a1ca-c0452307b979.gif)|
|합/불합 처리|이력서 수정|
|![합불합처리](https://user-images.githubusercontent.com/118786401/232636507-b8b4366b-04b3-4b1d-89f3-3d352f271c16.gif)|![이력서수정](https://user-images.githubusercontent.com/118786401/232636544-794341f5-ec36-44f5-9945-2702d10704db.gif)|
|북마크 기능|지원 신청|
|![북마크기능](https://user-images.githubusercontent.com/118786401/232636566-eba7e6a0-8d4b-47bd-b8c9-041fe1bee583.gif)|![지원신청](https://user-images.githubusercontent.com/118786401/233406164-9168732d-8ead-4d2d-bcb7-83ad7f8bc974.gif)|

## 테이블 설계
![테이블](https://user-images.githubusercontent.com/118786401/233412160-0d63fe4b-acb1-4d0e-b077-1d38cb985bfe.png)

## 블로깅
https://spark-mailbox-fe3.notion.site/K-Digital-1-5690c9f9f2db4c17ba8ede0b97ccc4e7
https://blog.naver.com/aozp73/223016815011

## 1차와의 변경점
- Controller 전체를 Rest화
- Jwt 추가
- 인증, 인가 처리 변경
- 유효성 검사 추가

## 구현하지 못한 기능
- 회사 별점
- 지도 API
- 주소입력 API
- 이메일 유효성 검사
