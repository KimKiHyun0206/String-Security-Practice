package com.springsecuritypractice.csrf;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.web.csrf.DefaultCsrfToken;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CsrfTokenEntity {
    @Id
    @Column(name = "csrf_id")
    private String id; // 세션ID
    private String token;
    private LocalDateTime createdAt;


    public DefaultCsrfToken toDefaultCsrfToken() {
        return new DefaultCsrfToken("X-CSRF-TOKEN", "_csrf", token);
    }
}
