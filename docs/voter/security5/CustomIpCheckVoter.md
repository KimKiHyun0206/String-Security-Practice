# CustomIpCheckVoter

> 이 클래스 `CustomIpCheckVoter`는 **Spring Security 5 방식의 `AccessDecisionVoter`를 구현한 클래스**로, **요청자의 IP 주소를 검사해서 접근을 허용 또는
거부하는 역할**을 합니다.

## 상세 설명

### 클래스:

```java
public class CustomIpCheckVoter implements AccessDecisionVoter<Object>{
    // 내부 구현 생략
}
```

* Spring Security의 인가 심사 방식 중 하나인 **Voter 기반 인가**에서 IP 주소 기반 검사를 담당합니다.

### 메서드 설명

#### 1. `vote(...)`

```java
public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> attributes){
    //  내부 구현 생략
}
```

* `FilterInvocation` 객체에서 IP 주소를 꺼낸다.

  ```java
  FilterInvocation fi = (FilterInvocation) object;
  String ip = fi.getHttpRequest().getRemoteAddr();
  ```

* 아래 조건이면 `ACCESS_GRANTED`:

  ```java
  if ("127.0.0.1".equals(ip) || "::1".equals(ip))
  ```

* 그 외 IP는 `ACCESS_DENIED` 리턴

* 즉, **로컬 요청만 허용**하는 Voter입니다.

#### 2. `supports(ConfigAttribute attribute)`

* 이 Voter는 모든 `ConfigAttribute`를 다룰 수 있다고 선언한다. → 필요 시 특정 속성(`IP_CHECK` 등)만 처리하도록 조건을 추가할 수 있습니다.

#### 3. `supports(Class<?> clazz)`

* `FilterInvocation` 타입의 객체만 처리 → 즉, **웹 요청(URL 접근)에 대해서만 작동**하는 Voter입니다.

## 예시 용도

* 로컬에서만 접근 가능한 관리자 페이지 제한
* 특정 IP 범위에만 열어두는 보안 정책 적용
* 개발환경/운영환경 분리 인가

## 정리

| 항목    | 설명                                          |
|-------|---------------------------------------------|
| 목적    | 요청자의 IP가 로컬인지 판단해 접근 허용 여부 결정               |
| 방식    | Spring Security 5의 `AccessDecisionVoter` 기반 |
| 허용 조건 | `127.0.0.1` 또는 `::1` 인 경우만 허용               |
| 사용 위치 | `AccessDecisionManager`에 등록되어 작동            |

