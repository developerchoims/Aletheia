# 프로젝트 구조 간단 요약 :star2:

- alatheia 폴더 하위에 server-a(grpc client)폴더와 server-b(grpc server)폴더가 있습니다.
- server-a에서는 주문이 이루어집니다.(order관련 crud)
- server-a, server-b에서 login과 JWT Token 인증 관련 로직이 복합적으로 일어납니다.
- server-a에 존재하는 JWT Filter가 로그인(스웨거 관련 request 포함)을 제외한 모든 API Request가 있을 때마다 server-b에 JWT Token 인증을 요청합니다.
- server-b는 server-a에서 받은 데이터를 바탕으로 JWT Token을 발급하거나 유효성을 검사합니다.
<br/><br/><br/><br/>
# Quick Start :runner:
### 프로젝트 흐름 (실행 과정 - 클릭 시 관련 postman주소로 넘어갑니다.)

1. secret.env를 서버에 알맞게 적용해주세요.(server-b에 server-a의 secret.env가 적용되지 않도록 해주세요.)<br/>
(secret.env 존재 위치)
![image](https://github.com/user-attachments/assets/84c57611-d649-454c-b01f-023d591d98b8)

2. .sql파일을 이용해 server-a에 DB를 구축합니다.-> 글자가 깨지지 않도록 언어 설정에 신경써 주세요.스키마:server-a (server-b DB는 사용하지 않기 때문에 없습니다.)

3. server-a, server-b 모두 run 상태로 만들어주세요.

4. [로그인 {userId, password} = {"ms123", "ms123ms123!"}](https://web.postman.co/workspace/GeumBang~af1d07bc-b5a2-4f93-a1ac-339f472734ae/collection/37825355-7925a5c2-82df-4ee1-9b3d-0bf808b2d2e6)
  
5. [주문 (주문, 주문상태 변경, 주문 목록 조회 테스트 가능, 데이터 입력 방식은 해당 페이지에서 확인 가능합니다.)](https://web.postman.co/workspace/GeumBang~af1d07bc-b5a2-4f93-a1ac-339f472734ae/collection/37825355-f5c4092b-b055-4abc-9966-32f0f134f7ea)
   
6. [GRPC (토큰 유효성 검사, 토큰 발급 확인 테스트 가능합니다.)](https://web.postman.co/workspace/GeumBang~af1d07bc-b5a2-4f93-a1ac-339f472734ae/collection/66dfdfed87e11a120becd07b)

7. swagger를 확인하기 위해서는 SecurityConfig와 JwtFilter의 url 주석을 해제해주세요.

### postman과 관련된 자세한 사항은 맨 마지막 단락에 존재합니다. 해당 단락을 참고해주세요.<br/><br/><br/><br/>

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
  ![image](https://github.com/user-attachments/assets/2f090443-eeac-4258-8a67-23616101affa)



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
### 안내 사항
- {userId, password}의 형태로 body(raw)에 값을 입력하고 바꿀 수 있습니다.
- 현재 DB에 저장된 값은 테스트용으로 삽입된 {"ms123", "ms123ms123!"} 데이터 하나입니다.
- server-a, server-b 가 모두 run 상태여야 정상 작동합니다.
- 로그인 실패 시 - JWT Token이 생성되지 않습니다.
- 로그인 성공 시 - JWT Token이 생성됩니다. ( Access Token - Header, Refresh Token - Cookie)
### 요청 주소
- http://localhost:{env}/api/login
- 예시 RequestBody
```
{"userId" : "ms123",
"password" : "ms123ms123!"}!
```
### 테스트 중점
존재하지 않는 userId 입력 시, password를 잘못 입력 시 다른 문구가 출력되는지 확인

## GRPC
- https://web.postman.co/workspace/GeumBang~af1d07bc-b5a2-4f93-a1ac-339f472734ae/collection/66dfdfed87e11a120becd07b
### 안내 사항
- login test와 order test 진행하며 자동으로 테스트 되는 검사 목록들입니다.
- ValidateAcsToken_ValidateRfrToken test : 토큰 유효성 검사 가능
- generateToken test : 토큰 발급 확인 가능

### ValidateAcsToken_ValidateRfrToken test
요청 주소
- localhost:{env}
- 데이터 형식
```
{"accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoi7ZqM7JuQIiwidXNlcklkIjoibXMxMjMiLCJpYXQiOjE3MjU5NDkxNDQsImV4cCI6MTcyNTk1MDA0NH0.dGJ8gbl1CkSFVrZxUjVGiKgmJ5UPoTuOzUuLFPjDc9k"
, "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoi7ZqM7JuQIiwidXNlcklkIjoibXMxMjMiLCJpYXQiOjE3MjU5NDkxNDQsImV4cCI6MTcyNjU1Mzk0NH0.XaOn--AYc1yxfsE61BW7eGP1HruvjeJMOv3KP88TgzI"}
```
테스트 중점
- 확인하실 시에는 이미 만료되었을 가능성이 크므로 login test 이후 생성된 token 검사를 하시는 편을 권장드립니다.

### generateToken test
요청 주소
- localhost:{env}
- 데이터 형식
```
{"userId": "ms123"
, "role": "회원"}
```
- 테스트 중점
server-a에서 로그인 검사를 마치고 넘긴 데이터를 입력하기 때문에 잘못된 userId, role이 입력되지 않는다는 가정하에 테스트를 진행합니다.

## 주문
- https://web.postman.co/workspace/GeumBang~af1d07bc-b5a2-4f93-a1ac-339f472734ae/collection/37825355-f5c4092b-b055-4abc-9966-32f0f134f7ea
### 안내 사항
- 주문과 관련된 api를 test할 수 있는 collection입니다.
- order_post test : user가 물건을 구매할 경우 사용되는 api test
- order_put test : admin이 주문 상태 및 판매 상태를 변경할 경우 사용되는 api test
- order_get test : user가 구매/판매 목록을 불러옴, 구매-자신 것만 열람 가능, 판매 - 모두 가능(아직 판매되지 않은 선에서)
- purchase_post test : 판매 요청 테스트

api가 많지 않기 때문에 요청 주소는 restfult API 의 원칙을 준수하기 위하여 요청주소는 같되 get, post 등을 달리하여 mapping하였습니다.
### order_post test
요청 주소
- http://localhost:9999/api/order
데이터 형식
```
{ "userId": "ms123"
, "addressId": 1
, "transactions": "구매"
, "transactionsNumber": "ms123-2409191340"
, "orderDetail": [{ "itemId": 1, "totalPrice": 100000, "quantity": 100 }]
}
```
테스트 중점
- 존재하지 않는 userId 입력
- 존재하지 않는 addressId 입력(1까지만 존재합니다.)
- 존재하지 않는 itemId 입력 ( 1, 2만 존재합니다.)
- quantity를 소수점 2자리 수 이상 입력

주의 사항
- totla Price는 front에서 계산된 값이 넘어온다는 전제하에 임의로 값을 입력합니다.

### order_put test
요청 주소
- http://localhost:9999/api/order
데이터 형식
```
{    "orderId": 33
,    "orderNumber": "ms123-2409195941"
,    "status": "입금완료"
,    "statusChk": "송금완료"}
```
테스트 중점
- 존재하지 않는 orderId 입력
- 잘못된 형식의 status 입력
- 잘못된 형식의 statusChk 입력

주의 사항
- status, statusChk는 Enum type이므로 띄어쓰기가 추가되어도 데이터가 저장되지 않습니다.
- orderNumber가 반드시 들어가도록 했기 때문에 이를 비워놓으면 안 됩니다.

### order_get test
요청 주소
- http://localhost:9999/api/order
데이터 형식
```
RequestParam
[{"key":"userId","value":"ms123"},{"key":"search","value":"ms123"},{"key":"transactions","value":"구매"},{"key":"page","value":"0"},{"key":"size","value":"3"}]
```
테스트 중점
- 존재하지 않는 userId 입력
- page와 size의 값을 변화시키기



