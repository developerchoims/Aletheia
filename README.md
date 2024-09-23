> # 개인 간의 금 구매, 판매를 지원하는 C2C 서비스입니다.
----------------------------------------------------------

# 프로젝트 구조 간단 요약 :star2:

### 독립적으로 확장 가능한 마이크로서비스 아키텍처에 흥미를 가지고 gRPC를 이용해 구현했습니다.
- alatheia 폴더 하위에 server-a(grpc client)폴더와 server-b(grpc server)폴더가 있습니다.
- server-a에서는 주문이 이루어집니다.(order관련 crud)
- server-a, server-b에서 login과 JWT Token 인증 관련 로직이 복합적으로 일어납니다.
- server-a에 존재하는 JWT Filter가 로그인을 제외한 모든 API Request가 있을 때마다 server-b에 JWT Token 인증을 요청합니다.
- server-b는 server-a에서 받은 데이터를 바탕으로 JWT Token을 발급하거나 유효성을 검사합니다.
<br/><br/><br/><br/>

# 사용 기술
<div align='center'>
  <img src="https://img.shields.io/badge/IntelliJ-000000?style=for-the-badge&logo=IntelliJ-&logoColor=white">
  <img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=SpringBoot-&logoColor=white">
  <img src="https://img.shields.io/badge/SpringSecurity-6DB33F?style=for-the-badge&logo=SpringSecurity-&logoColor=white">
  <img src="https://img.shields.io/badge/JPA-6DB33F?style=for-the-badge&logo=JPA-&logoColor=white">
  <img src="https://img.shields.io/badge/Java-4B4B77?style=for-the-badge&logo=Java-&logoColor=white">
  <img src="https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=MariaDB-&logoColor=white">
  <img src="https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=GitHub-&logoColor=white">
  <img src="https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=Postman&logoColor=white">
  <img src="https://img.shields.io/badge/GRPC-40AEF0?style=for-the-badge&logo=GRPC&logoColor=white">
</div>

<br/><br/><br/><br/>
# DB Diagram을 활용한 데이터 모델링 :card_index_dividers:
- https://dbdiagram.io/d/66e11838550cd927ead50df1
 ![image](https://github.com/user-attachments/assets/cf31c2cd-a2f5-4a40-94db-819d75588d6a)

<br/><br/><br/><br/>
# API 스펙이 담긴 Swagger :pencil:
### server-a
- http://localhost:9999/swagger-ui/index.html
### server-b
- http://localhost:8888/swagger-ui/index.html#/
<br/><br/><br/><br/>

# API 호출이 가능한 Postman링크 :computer:

## 로그인
- https://web.postman.co/workspace/GeumBang~af1d07bc-b5a2-4f93-a1ac-339f472734ae/collection/37825355-7925a5c2-82df-4ee1-9b3d-0bf808b2d2e6

## GRPC
- https://web.postman.co/workspace/GeumBang~af1d07bc-b5a2-4f93-a1ac-339f472734ae/collection/66dfdfed87e11a120becd07b

## 주문
- https://web.postman.co/workspace/GeumBang~af1d07bc-b5a2-4f93-a1ac-339f472734ae/collection/37825355-f5c4092b-b055-4abc-9966-32f0f134f7ea



