# 주요 역할

> AccessDecisionManagerWrapper는 레거시 방식의 AccessDecisionManager를 Spring Security 6의 AuthorizationManager와 호환되도록 감싸는 래퍼 클래스로, URI 기반 권한 판정을 수행해 인가 여부를 결정한다.

| 역할          | 설명                                                                                    |
|-------------|---------------------------------------------------------------------------------------|
| 래퍼(Wrapper) | 기존 `AccessDecisionManager`를 Spring Security 6의 `AuthorizationManager`로 감싸 재활용 가능하게 만듦 |
| 인가 판단       | URI에 따라 필요한 권한(`ConfigAttribute`)을 결정하고, 인증 객체(`Authentication`)와 비교해 인가 여부 판단        |
| 인가 실패 처리    | `AccessDeniedException`이 발생하면 false를 반환해 거부 처리                                        |

---

# 클래스 구조
```java
public class AccessDecisionManagerWrapper 
        implements AuthorizationManager<RequestAuthorizationContext>{
    // 내부 구현 생략
}
```
* `AuthorizationManager<RequestAuthorizationContext>`를 구현하여 스프링 시큐리티 6의 새 인가 시스템에서 사용할 수 있다.
* 하지만 내부적으로는 기존 `AccessDecisionManager`를 활용한다.
    * 이는 레거시 시스템 호환 목적으로 유용하다.

### check()
```java
public AuthorizationDecision check(
        Supplier<Authentication> authentication, 
        RequestAuthorizationContext context
) {
  try {
    String uri = context.getRequest().getRequestURI();
    Collection<ConfigAttribute> attributes = determineAttributesByUri(uri);
    accessDecisionManager.decide(authentication.get(), context, attributes);
    return new AuthorizationDecision(true);
  } catch (AccessDeniedException ex) {
    return new AuthorizationDecision(false);
  }
}
```
* 요청 URI를 바탕으로 필요한 권한(ConfigAttribute)을 추론한다.
* `accessDecisionManager.decide()`를 호출해 인가 여부를 판단한다.
* 성공 시 `new AuthorizationDeicision(true)`, 실패시 `false`

### determineAttributesByUri()
```java
private Collection<ConfigAttribute> determineAttributesByUri(String uri) {
    if (uri.startsWith("/api/admin")) {
        return Collections.singletonList(() -> "ROLE_ADMIN");
    }
    return Collections.emptyList();
}
```
* 인가 판단에 필요한 권한(ConfigAttribute)을 직접 URI 기반으로 설정한다.
* ex) `/api/admin`으로 시작하면 `ROLE_ADMIN`권한 필요.