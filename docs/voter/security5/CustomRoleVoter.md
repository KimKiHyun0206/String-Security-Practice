# CustomRoleVoter
> CustomRoleVoter는 요청에 필요한 역할 권한(ROLE_)과 사용자 권한을 비교하여, 접근 허용 여부를 판단하는 Spring Security 5 스타일의 Voter 클래스이다.


* `Authentication`객체의 권한과 `ConfigAttribute`(요청에 필요한 권한)을 비교한다.
* 일치하는 권한이 있으면 `ACCESS_GRANTED`를 반환하고 권한이 없으면 `ACCESS_DENIED`를 반환한다.

<br>

## 검사 흐름 요약
1. 인증 객체가 null이거나 인증되지 않았으면 `ACCESS_DENIED`
2. 요청에 지정된 권한(`ConfigAttribute`)중 `ROLE_`로 시작하는 항목만 필터링
3. 사용자 권한 목록(`GrantedAuthority`)에 해당 역할이 있으면 `ACCESS_GRANTED` 없다면 `ACCESS_DENIED`

<br>

| 항목    | 설명                                                       |
| ----- | -------------------------------------------------------- |
| 역할    | `ROLE_`로 시작하는 권한 기준으로 접근 허용/거부 판단                        |
| 사용 맥락 | `AccessDecisionManager`에 등록되어 Spring Security 5 방식으로 사용됨 |
| 특징    | `supports()`를 통해 명확하게 자신의 책임 범위를 제한                      |
