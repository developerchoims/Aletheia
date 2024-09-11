# 프로젝트 구조 간단 요약

- alatheia 폴더 하위에 server-a(grpc client)폴더와 server-b(grpc server)폴더가 있습니다.
- server-a에서는 주문이 이루어집니다.(order관련 crud)
- server-a, server-b에서 login과 JWT Token 인증 관련 로직이 복합적으로 일어납니다.
- server-a에 존재하는 JWT Filter가 로그인을 제외한 모든 API Request마다 server-b에 JWT Token 인증을 요청합니다.
- server-b는 server-a에서 받은 데이터를 바탕으로 JWT Token을 발급하거나 유효성을 검사합니다.

# Quick Start :runner:
### 프로젝트 흐름 (실행 과정 - 클릭 시 관련 postman주소로 넘어갑니다.)

1. secret.env를 서버에 알맞게 적용해주세요.(server-b에 server-a의 secret.env가 적용되지 않도록 해주세요.)

2. server-a, server-b 모두 run 상태로 만들어주세요.

3. [로그인 {userId, password} = {"ms123", "ms123ms123!"}](https://web.postman.co/workspace/GeumBang~af1d07bc-b5a2-4f93-a1ac-339f472734ae/collection/37825355-7925a5c2-82df-4ee1-9b3d-0bf808b2d2e6)
  
4. [주문 (주문, 주문상태 변경, 주문 목록 조회 테스트 가능, 데이터 입력 방식은 해당 페이지에서 확인 가능합니다.)](https://web.postman.co/workspace/GeumBang~af1d07bc-b5a2-4f93-a1ac-339f472734ae/collection/37825355-f5c4092b-b055-4abc-9966-32f0f134f7ea)
   
5. [GRPC (토큰 유효성 검사, 토큰 발급 확인 테스트 가능합니다.)](https://web.postman.co/workspace/GeumBang~af1d07bc-b5a2-4f93-a1ac-339f472734ae/collection/66dfdfed87e11a120becd07b)


# DB Diagram을 활용한 데이터 모델링 :card_index_dividers:



# API 스펙이 담긴 Swagger :pencil:



# API 호출이 가능한 Postman링크 :computer:
