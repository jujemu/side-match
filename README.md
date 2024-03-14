# toy-project-platform

## API 문서

<a href="https://www.notion.so/Rest-API-3132ab1f13a54100887add988d294108?pvs=4">
  Notion link  
</a>
<div>
  <br>
  <img src="https://github.com/jujemu/side-match/assets/103489171/26b7aba9-59aa-43d2-b247-07a6e546aa80" width="90%">
</div>
  
## 기획 개요

<a href="https://docs.google.com/presentation/d/1Yscm7k98Vgrge_egP0EVL1aXXOW2gEJyWoieuwWWqPg/edit?usp=sharing">
  Google slide
</a>

## 아키텍쳐

사진

## ERD

사진

## Login Flow

### UserInfo endpoint

![](./images/oauth_flow_After_accessToken.png)

- Authorization Server로부터 access token을 받고 나면 위의 흐름대로 Resource owner의 동의된 정보를 가져오도록 구현
- 유저 정보가 등록되어 있지 않으면 userService.signUp()으로 user record 생성
- 성공적으로 SecurityContext에 Principal를 저장하면 OAuth2SuccessHandler에서 JWT를 생성하고 응답

### Validate Token

![](./images/validate_token.png)

- 브라우저의 쿠키에 저장된 token을 이용해서 endpoint로 접근하면 JwtAuthenticationFilter에서 유효성을 검증
- 검증된 후에는 UsernamePasswordToken을 생성하고 SecurityContextHolder에 저장
- UsernamePasswordToken은 JWT와 관련 있는 것이 아니라 스프링 시큐리티에서 제공하는 Principal 구현체라는 점을 주의

## CI/CD 구축 과정

사진

- https://jujemu.tistory.com/34
- https://jujemu.tistory.com/35

## 회고

### OAuth 인증 후에 토큰 발급 테스트

https://jujemu.tistory.com/106

### 동적 쿼리를 if문으로 작성하여 지저분했던 코드를 QueryDSL 개선

https://jujemu.tistory.com/116

### mysql에 저장할 때, 아이콘, 이모지가 저장 안 되는 문제 해결

https://jujemu.tistory.com/63

### 사이트가 한국 시간대로 표시되지 않던 문제 해결

- 비교적 간단한 문제임에도 꽤나 애먹었던 것으로 기억한다.
- 문제의 원인이 어디인지 명확하게 분석하고 해결하는 것이 중요하다는 것을 배웠다.
- 원인 분석을 제대로 하지 않아, aws ec2 시간대와 프론트 처리 로직 재검토, spring LocalDateTime 을 수정하고, db 설정까지 다 건드리면서 해결했다.

---

### 토큰 발급 과정에서 보안 취약점 해결

MVP 배포 이전에 이용약관 페이지를 추가했다.  
이용약관은 서비스 이용에 필요한 최소한의 개인 정보를 보관 동의와 기타 사항들을 포함하고 있었다.  
문제는 외부 인증 서버에서 리다이렉트로 이용약관 페이지를 응답하는 과정에 보안 취약점이 생겼다.  

기존에는 로그인 페이지 이후에 바로 추가 정보 입력 페이지와 함께 토큰을 응답했다.  
많은 유저가 추가 정보 제공에 거부감을 느끼고 메인 페이지로 넘어갈 것이라 판단해서 토큰을 포함하여 응답했다.  

하지만 중간에 이용약관 페이지을 추가하면서 추가 정보 페이지에 넘어가기 위해 사용자를 식별하려고 이메일을 인자로 받았다.  
QA를 통해 사용자의 이메일만 알면 토큰을 발급 받을 수 있는 취약점을 확인했다.  

문제 해결을 위해 이용약관에 동의하지 않은 유저와 최초 접근 사용자에 한해서  
이메일을 대신할 UUID를 생성했고 이후 사용자는 UUID를 통해 접근할 수 없도록 방지 로직을 추가로 구현했다.  

프론트와 백엔드가 API를 통해 통신하는 과정에서 보안 취약점이 생길 수 있음을 배웠고  
로직 개선 뿐만 아니라 동료와 함께 취약점에 대해 얘기해볼 수 있는 좋은 경험이었다.

---
