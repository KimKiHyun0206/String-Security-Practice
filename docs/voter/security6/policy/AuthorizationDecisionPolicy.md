# AuthorizationDecisionPolicy

* 여러 `voter`들이 낸 투표 결과를 받아서 특정 정책에 따라 최종적으로 인가를 허용할지 또는 거부할지 결정한다.
* 입력: 여러 투표자가 낸 투표 결과 리스트. 여기서 각 정수는 `ACCESS_GRANT`,`ACCESS_DENIED`, `ACCESS_ABSTAIN` 중 하나를 의미한다.
* 출력: 프링 시큐리티의 `AuthorizationDecision`객체(인가 허용 여부 포함)

구현체가 세 개가 있는데 세 개는 각각 정책을 구현한 구현체이다.ㅊ