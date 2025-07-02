# CustomAuthorizationManager

> 여러 투표자의 인가 판단 결과를 모아, 주입받은 정책에 따라 최종 인가 여부를 결정하는 Spring Security 6용 인가 매니저 클래스이다.

1. **복수의 `CustomAuthorizationVoter`를 관리한다.**
    - 여러 개의 커스텀 투표자에게 인증 정보(Authorization)와 요청 정보(HttpServletRequest)를 전달해 인가 투표를 요청한다.
2. **각 Voter가 내린 투표 결과를 수집한다.**
    - `voter.vore(auth.request)`호출 결과인 `ACCESS_GRANTED`, `ACCESS_DENIED`, `ACCESS_ABSTAIN` 같은 결과들을 리스트로 모은다.
3. **투표 결과를 `AuthorizationDecisionPolicy`에 위임해 최종 인가를 결정한다.**
    - 투표 결과 리스트를 정책 객체(policy.decide(vote))에 넘겨 정책에 따라 최종적으로 허용 또는 거부한다.
4. **`AuthorizationManager<RequestAuthorizationContext>`인터페이스 구현.**
    - 스프링 시큐리티 6에서 사용하는 표준 인가 관리자 역할을 하며 `check()`메소드를 통해 인가 여부를 판단하는 구조이다.ㅇ