# 클래스 설명
```java
@Slf4j
public class AdminFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String path = request.getRequestURI();

        // 특정 URI에 인증된 사용자만 접근 허용
        if (path.startsWith("/api/admin/")) {
            log.info("[AdminFilter] request URL {}", path);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("인증되지 않은 사용자입니다.");
                return;
            }

            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

            if (!isAdmin) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("관리자 접근 권한이 없습니다.");
                return;
            }
        }


        // 필터 체인 계속 진행
        filterChain.doFilter(request, response);
    }
}
```
* `OncePerRequestFilter`: 요청당 한 번만 실행되는 필터(중복 실행 방지)
* 목적: 관리자 전용 URL 요청을 필터링하여, 인증 여부와 관리자 권한 보유 여부를 검사한다.

---

# 동작 요약
1. 요청 URI 검사
2. 인증 여부 확인
3. 권한 검사(인가)
4. 요청 계속 진행

