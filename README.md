# 프로젝트 구조 간단 요약 :star2:

- alatheia 폴더 하위에 server-a(grpc client)폴더와 server-b(grpc server)폴더가 있습니다.
- server-a에서는 주문이 이루어집니다.(order관련 crud)
- server-a, server-b에서 login과 JWT Token 인증 관련 로직이 복합적으로 일어납니다.
- server-a에 존재하는 JWT Filter가 로그인(스웨거 관련 request 포함)을 제외한 모든 API Request가 있을 때마다 server-b에 JWT Token 인증을 요청합니다.
- server-b는 server-a에서 받은 데이터를 바탕으로 JWT Token을 발급하거나 유효성을 검사합니다.
<br/><br/><br/><br/>
# Quick Start :runner:
### 프로젝트 흐름 (실행 과정 - 클릭 시 관련 postman주소로 넘어갑니다.)

  ![image](https://github.com/user-attachments/assets/c42f9865-87c8-4e58-9347-c9d46554b0e8)
<div align='center'>
<svg role="img" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><title>IntelliJ IDEA</title><path d="M0 0v24h24V0zm3.723 3.111h5v1.834h-1.39v6.277h1.39v1.834h-5v-1.834h1.444V4.945H3.723zm11.055 0H17v6.5c0 .612-.055 1.111-.222 1.556-.167.444-.39.777-.723 1.11-.277.279-.666.557-1.11.668a3.933 3.933 0 0 1-1.445.278c-.778 0-1.444-.167-1.944-.445a4.81 4.81 0 0 1-1.279-1.056l1.39-1.555c.277.334.555.555.833.722.277.167.611.278.945.278.389 0 .721-.111 1-.389.221-.278.333-.667.333-1.278zM2.222 19.5h9V21h-9z"/></svg>
<svg role="img" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><title>Spring Boot</title><path d="m23.693 10.7058-4.73-8.1844c-.4094-.7106-1.4166-1.2942-2.2402-1.2942H7.2725c-.819 0-1.8308.5836-2.2402 1.2942L.307 10.7058c-.4095.7106-.4095 1.873 0 2.5837l4.7252 8.189c.4094.7107 1.4166 1.2943 2.2402 1.2943h9.455c.819 0 1.826-.5836 2.2402-1.2942l4.7252-8.189c.4095-.7107.4095-1.8732 0-2.5838zM10.9763 5.7547c0-.5365.4377-.9742.9742-.9742s.9742.4377.9742.9742v5.8217c0 .5366-.4377.9742-.9742.9742s-.9742-.4376-.9742-.9742zm.9742 12.4294c-3.6427 0-6.6077-2.965-6.6077-6.6077.0047-2.0896.993-4.0521 2.6685-5.304a.8657.8657 0 0 1 1.2142.1788.8657.8657 0 0 1-.1788 1.2143c-2.1602 1.6048-2.612 4.6592-1.0072 6.8194 1.6049 2.1603 4.6593 2.612 6.8195 1.0072 1.2378-.9177 1.9673-2.372 1.9673-3.9157a4.8972 4.8972 0 0 0-1.9861-3.925c-.386-.2824-.466-.8284-.1836-1.2143.2824-.386.8283-.466 1.2143-.1835 1.6895 1.2471 2.6826 3.2238 2.6873 5.3228 0 3.6474-2.965 6.6077-6.6077 6.6077z"/></svg>
<svg role="img" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><title>Spring Security</title><path d="M20.59 2.066 11.993 0 3.41 2.066v6.612h4.557a3.804 3.804 0 0 0 0 .954H3.41v3.106C3.41 19.867 11.994 24 11.994 24s8.582-4.133 8.582-11.258V9.635h-4.545a3.616 3.616 0 0 0 0-.954h4.558zM12 12.262h-.006a3.109 3.109 0 1 1 .006 0zm-.006-4.579a.804.804 0 0 0-.37 1.52v.208l.238.237v.159l.159.159v.159l-.14.14.15.246v.159l-.16.189.223.222.246-.246V9.218a.804.804 0 0 0-.346-1.535zm0 .836a.299.299 0 1 1 .298-.299.299.299 0 0 1-.298.3z"/></svg>
<svg role="img" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><title>GitHub</title><path d="M12 .297c-6.63 0-12 5.373-12 12 0 5.303 3.438 9.8 8.205 11.385.6.113.82-.258.82-.577 0-.285-.01-1.04-.015-2.04-3.338.724-4.042-1.61-4.042-1.61C4.422 18.07 3.633 17.7 3.633 17.7c-1.087-.744.084-.729.084-.729 1.205.084 1.838 1.236 1.838 1.236 1.07 1.835 2.809 1.305 3.495.998.108-.776.417-1.305.76-1.605-2.665-.3-5.466-1.332-5.466-5.93 0-1.31.465-2.38 1.235-3.22-.135-.303-.54-1.523.105-3.176 0 0 1.005-.322 3.3 1.23.96-.267 1.98-.399 3-.405 1.02.006 2.04.138 3 .405 2.28-1.552 3.285-1.23 3.285-1.23.645 1.653.24 2.873.12 3.176.765.84 1.23 1.91 1.23 3.22 0 4.61-2.805 5.625-5.475 5.92.42.36.81 1.096.81 2.22 0 1.606-.015 2.896-.015 3.286 0 .315.21.69.825.57C20.565 22.092 24 17.592 24 12.297c0-6.627-5.373-12-12-12"/></svg>
<svg role="img" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><title>Postman</title><path d="M13.527.099C6.955-.744.942 3.9.099 10.473c-.843 6.572 3.8 12.584 10.373 13.428 6.573.843 12.587-3.801 13.428-10.374C24.744 6.955 20.101.943 13.527.099zm2.471 7.485a.855.855 0 0 0-.593.25l-4.453 4.453-.307-.307-.643-.643c4.389-4.376 5.18-4.418 5.996-3.753zm-4.863 4.861l4.44-4.44a.62.62 0 1 1 .847.903l-4.699 4.125-.588-.588zm.33.694l-1.1.238a.06.06 0 0 1-.067-.032.06.06 0 0 1 .01-.073l.645-.645.512.512zm-2.803-.459l1.172-1.172.879.878-1.979.426a.074.074 0 0 1-.085-.039.072.072 0 0 1 .013-.093zm-3.646 6.058a.076.076 0 0 1-.069-.083.077.077 0 0 1 .022-.046h.002l.946-.946 1.222 1.222-2.123-.147zm2.425-1.256a.228.228 0 0 0-.117.256l.203.865a.125.125 0 0 1-.211.117h-.003l-.934-.934-.294-.295 3.762-3.758 1.82-.393.874.874c-1.255 1.102-2.971 2.201-5.1 3.268zm5.279-3.428h-.002l-.839-.839 4.699-4.125a.952.952 0 0 0 .119-.127c-.148 1.345-2.029 3.245-3.977 5.091zm3.657-6.46l-.003-.002a1.822 1.822 0 0 1 2.459-2.684l-1.61 1.613a.119.119 0 0 0 0 .169l1.247 1.247a1.817 1.817 0 0 1-2.093-.343zm2.578 0a1.714 1.714 0 0 1-.271.218h-.001l-1.207-1.207 1.533-1.533c.661.72.637 1.832-.054 2.522zM18.855 6.05a.143.143 0 0 0-.053.157.416.416 0 0 1-.053.45.14.14 0 0 0 .023.197.141.141 0 0 0 .084.03.14.14 0 0 0 .106-.05.691.691 0 0 0 .087-.751.138.138 0 0 0-.194-.033z"/></svg>
<div/>

1. secret.env를 서버에 알맞게 적용해주세요.(server-b에 server-a의 secret.env가 적용되지 않도록 해주세요.)<br/>
(secret.env 존재 위치)
![image](https://github.com/user-attachments/assets/84c57611-d649-454c-b01f-023d591d98b8)


3. server-a, server-b 모두 run 상태로 만들어주세요.

4. [로그인 {userId, password} = {"ms123", "ms123ms123!"}](https://web.postman.co/workspace/GeumBang~af1d07bc-b5a2-4f93-a1ac-339f472734ae/collection/37825355-7925a5c2-82df-4ee1-9b3d-0bf808b2d2e6)
  
5. [주문 (주문, 주문상태 변경, 주문 목록 조회 테스트 가능, 데이터 입력 방식은 해당 페이지에서 확인 가능합니다.)](https://web.postman.co/workspace/GeumBang~af1d07bc-b5a2-4f93-a1ac-339f472734ae/collection/37825355-f5c4092b-b055-4abc-9966-32f0f134f7ea)
   
6. [GRPC (토큰 유효성 검사, 토큰 발급 확인 테스트 가능합니다.)](https://web.postman.co/workspace/GeumBang~af1d07bc-b5a2-4f93-a1ac-339f472734ae/collection/66dfdfed87e11a120becd07b)

### postman과 관련된 자세한 사항은 맨 마지막 단락에 존재합니다. 해당 단락을 참고해주세요.
<br/><br/><br/><br/>
# DB Diagram을 활용한 데이터 모델링 :card_index_dividers:
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
- order_post test : user가 물건을 주문할 경우 사용되는 api test
- order_put test : admin이 주문 상태 및 판매 상태를 변경할 경우 사용되는 api test
- order_get test : user가 자신의 주문 목록을 불러올 경우 사용, admin이 특정 사용자의 주문 목록을 불러올 경우 사용되는 api test
- api가 많지 않기 때문에 요청 주소는 restfult API 의 원칙을 준수하기 위하여 요청주소는 같되 get, post 등을 달리하여 mapping하였습니다.

### order_post test
요청 주소
- http://localhost:{env}/api/order
데이터 형식
```
{"userId": "ms123"
, "addressId": 1
, "orderDetail": [{ "itemId": 2, "totalPrice": 100000, "quantity": 4}]
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
- http://localhost:{env}/api/order
데이터 형식
```
{"orderId": 4
, "status": "입금완료"
, "statusChk": "송금완료"}
```
테스트 중점
- 존재하지 않는 orderId 입력
- 잘못된 형식의 status 입력
- 잘못된 형식의 statusChk 입력

주의 사항
- status, statusChk는 Enum type이므로 띄어쓰기가 추가되어도 데이터가 저장되지 않습니다.

### order_get test
요청 주소
- http://localhost:{env}/api/order
데이터 형식
```
RequestParam
[{"key":"userId","value":"ms123"}
,{"key":"page","value":"0"}
,{"key":"size","value":"3"}]
```
테스트 중점
- 존재하지 않는 userId 입력
- page와 size의 값을 변화시켜 확인





