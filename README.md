# 스프링 시큐리티 학습용 레포지토리

> 스프링 시큐리티의 다양한 기능을 구현해보며 학습하기 위한 레포지토리

<br>

# main

* 세션 기반 인증
* `UsernamePasswordAuthenticationFilter`를 사용하여 로그인할 수 있도록 함.
     * 로그인에 필요한 api를 따로 구현하지 않아도 된다.

<br>

### UsernamePasswordAuthenticationFilter
`UsernamePasswordAuthenticationFilter`가 로그인 요청(`/login` POST)을 가로채서 인증을 수행하면,

1. 사용자 아이디와 비밀번호를 `AuthenticationManager`에 넘겨서 인증 처리한다.
2. 인증에 성공하면 `Authentication` 객체가 생성된다.
3. 이 `Authentication` 객체는 **`SecurityContextHolder`에 저장**되어 현재 스레드의 보안 컨텍스트에 유지된다.
4. 이후 요청에서는 이 컨텍스트를 통해 인증된 사용자 정보를 확인할 수 있게 된다.


```plaintext
요청 → UsernamePasswordAuthenticationFilter → AuthenticationManager → 인증 성공 → SecurityContextHolder에 Authentication 저장
```

이후부터는 `SecurityContextHolder`가 인증된 사용자 정보의 **중앙 저장소 역할**을 하게 된다.
