# 📘 기초 프로젝트 복습 - 뉴스피드 프로젝트
- 회원 프로필 관리, 로그인/회원가입, 게시글, 댓글, 팔로우, 좋아요 기능이 있는 뉴스피드 서버 프로그램을 개발합니다.
- JPA를 바탕으로 뉴스피드 앱을 구현하는 과제입니다.
- 개발기간: 3/10(월) ~ 3/21(금)
- 개발환경
  - development: IntelliJ IDEA, git, github
  - environment: JAVA JDK 17, Spring Boot 3.4.2, JPA, MySQL

 <img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white"> <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"> <img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white"> <img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white"> <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"> <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">

 <br><br>

# 🖼️ API 명세서 및 ERD
 - API 명세서 및 ERD 링크: https://teamsparta.notion.site/API-ERD-API-ERD-1b22dc3ef514817b9444ce302ad1d3cd

<br><br>

# API 동작 캡처본
## 유저 API
- 회원가입
  ![image](https://github.com/user-attachments/assets/4ff63733-de85-4cbb-80d6-1271e48a8d3b)
  
- 로그인
  ![image](https://github.com/user-attachments/assets/b6e5b8c3-9e11-40e7-94d1-225f3078b8f8)
  
- 다른 유저 조회
  ![image](https://github.com/user-attachments/assets/97ebeb39-2083-40c8-abef-a50babd2a89b)

- 본인 조회
  ![image](https://github.com/user-attachments/assets/38d3c395-09eb-427a-a358-62022d6306c7)

- 유저 전체 조회
  ![image](https://github.com/user-attachments/assets/9cd7b251-fae2-4d42-b178-a24f2cfafb10)

- 본인 프로필 수정
  ![image](https://github.com/user-attachments/assets/17bb25ce-3eb6-43f1-942e-e8d624b8daac)

- 본인 비밀번호 수정
  ![image](https://github.com/user-attachments/assets/ed6afcc8-69cf-4e61-8f85-05889878e337)

- 유저 삭제
## 게시물 API
- 게시물작성
  ![image](https://github.com/user-attachments/assets/41dba436-89ab-46d4-b803-f0321266e5ba)

- 본인 게시물 전체 조회
  ![image](https://github.com/user-attachments/assets/20b1f040-1435-426a-9415-6e2001334368)

- 게시물 전체 조회 (생성일자 기준)
  ![image](https://github.com/user-attachments/assets/69d78485-7515-46a9-bbce-06f009efe889)

- 팔로우한 유저 게시물 전체 조회 (최신순)
  ![image](https://github.com/user-attachments/assets/770e687f-47dd-42ac-a76a-2f8af3d3203b)

- 게시물 전체 조회 (기간별 검색)
  ![image](https://github.com/user-attachments/assets/b40a3f20-29a9-4e9b-a9d4-b60bfdabe5df)

- 게시물 전체 조회 (수정일자 기준)
  ![image](https://github.com/user-attachments/assets/7bd7735a-3eed-4eeb-a47b-2284ba59afa8)

- 본인 게시물 수정
  ![image](https://github.com/user-attachments/assets/35ed2da1-ca47-431c-a7ee-6f74fe579d04)

- 본인 게시물 삭제
  ![image](https://github.com/user-attachments/assets/235bb171-7f5c-47a5-8fb1-1dfe451496ca)

## 팔로우 API
- 팔로우
  ![image](https://github.com/user-attachments/assets/387b077d-d25e-4a71-bf5b-95c030c9f77f)

- 팔로우 목록 조회
  ![image](https://github.com/user-attachments/assets/7ef770c6-3ffd-4203-a28e-7c949cf8038d)

- 언팔로우
  ![image](https://github.com/user-attachments/assets/962d4934-3e23-4b64-87b1-4114aeb8619c)

## 댓글 API
- 댓글 작성
  ![image](https://github.com/user-attachments/assets/916f3bf1-141d-4087-b785-0d80236a408a)

- 댓글 조회
  ![image](https://github.com/user-attachments/assets/458af694-2a00-4415-ad7e-5d405e0d405d)

- 본인 댓글 수정
  ![image](https://github.com/user-attachments/assets/3f939339-818e-4900-919f-2542e76bdfe6)

- 본인 댓글 삭제
  ![image](https://github.com/user-attachments/assets/a73e6868-c41a-4c7f-b38c-a66eb759fb9d)

## 좋아요 API
- 게시물 좋아요
  ![image](https://github.com/user-attachments/assets/a697315b-d00e-447d-920c-1301bc05799e)

- 게시물 좋아요 삭제
  ![image](https://github.com/user-attachments/assets/7e416410-6c79-430f-9b67-17e1763dd272)

- 댓글 좋아요
  ![image](https://github.com/user-attachments/assets/2af55572-db18-47d6-a0e1-f2820ca616ca)

- 댓글 좋아요 삭제
  ![image](https://github.com/user-attachments/assets/ed979d94-0bd8-43a2-a3d1-4a0f675918e3)

