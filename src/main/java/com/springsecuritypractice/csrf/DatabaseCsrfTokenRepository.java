package com.springsecuritypractice.csrf;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.DefaultCsrfToken;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
public class DatabaseCsrfTokenRepository implements CsrfTokenRepository {

    private final CsrfTokenJpaRepository repository;
    private final static String CSRF_TOKEN_HEADER = "X-CSRF-TOKEN";


    @Override
    public CsrfToken generateToken(HttpServletRequest request) {
        String tokenValue = UUID.randomUUID().toString();
        return new DefaultCsrfToken(CSRF_TOKEN_HEADER, "_csrf", tokenValue);
    }

    @Override
    public void saveToken(CsrfToken token, HttpServletRequest request, HttpServletResponse response) {
        String identifier = getTokenOwnerIdentifier(request);
        if (token == null) {
            repository.deleteById(identifier);
        } else {
            repository.save(
                    new CsrfTokenEntity(
                            identifier,
                            token.getToken(),
                            LocalDateTime.now()
                    )
            );
        }
    }

    @Override
    public CsrfToken loadToken(HttpServletRequest request) {
        String identifier = getTokenOwnerIdentifier(request);
        return repository.findById(identifier)
                .map(CsrfTokenEntity::toDefaultCsrfToken)
                .orElse(null);
    }

    // 만약 JWT 등 다른 인증 시스템과 같이 사용한다면 이 메소드는 필요없다.
    // 따라서 JWT 를 같이 사용한다면 위의 메소드도 변경되어야 한다.
    private String getTokenOwnerIdentifier(HttpServletRequest request) {
        // 예: 사용자 ID, 세션 ID, 쿠키 값 등
        return request.getSession().getId();
    }
}