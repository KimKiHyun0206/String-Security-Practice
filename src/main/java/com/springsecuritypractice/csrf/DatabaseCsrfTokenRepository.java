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


    @Override
    public CsrfToken generateToken(HttpServletRequest request) {
        String tokenValue = UUID.randomUUID().toString();
        return new DefaultCsrfToken("X-CSRF-TOKEN", "_csrf", tokenValue);
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


    private String getTokenOwnerIdentifier(HttpServletRequest request) {
        // 예: 사용자 ID, 세션 ID, 쿠키 값 등
        return request.getHeader("X-CSRF-ID");
    }
}