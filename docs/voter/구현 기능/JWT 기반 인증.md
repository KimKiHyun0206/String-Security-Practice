# 관련 클래스

* JwtController
* JwtSecurityConfig
* JwtFilter
* JwtTokenProvider

---

# 기능 설명

* 로그인에 성공하면 JWT를 반환한다.
* 토큰에는 `username`, `auth`가 있으며 `auth`에서 `ROLE_USER`와 같은 권한을 가지고 요청이 유효한지 판단한다.
* 요청 헤더에 `Authorization: Bearer <token>`형식으로 오면 `JwtFilter`가 이를 확인하여 토큰으로부터 `Authorization`을 얻고 `SecurityContextHolder`에
  저장한다.

---

# 클래스 설명

* **JwtController**: JWT 기능에서 로그인 요청을 수행해줄 컨트롤러.
* **JwtSecurityConfig**: `JwtFilter`를 `UsernamePasswordAuthenticationFilter` 전에 삽입하고 사용해야 할 URL(Swagger와 컨트롤러 URL)을
  허용해준다.
* **JwtFilter**: 요청을 받으면 중간에서 헤더에 JWT가 있는지 검사하고 있다면 토큰에서 `Authentication`을 얻어서 `SecurityContextHolder`에 저장한다.
* **JwtTokenProvider**: 로그인이 성공하면 토큰을 발급해주는 클래스이다. 그리고 JWT 토큰을 넘기면 `Authentication`을 얻는 역할도 한다.