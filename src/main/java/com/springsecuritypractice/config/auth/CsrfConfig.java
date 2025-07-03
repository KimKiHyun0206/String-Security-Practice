package com.springsecuritypractice.config.auth;

import com.springsecuritypractice.config.filter.CsrfHeaderWriterFilter;
import com.springsecuritypractice.csrf.CsrfTokenJpaRepository;
import com.springsecuritypractice.csrf.DatabaseCsrfTokenRepository;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;

@EnableWebSecurity
@RequiredArgsConstructor
public class CsrfConfig {
    private final CsrfTokenJpaRepository csrfTokenJpaRepository;
    private final CsrfHeaderWriterFilter csrfHeaderWriterFilter;

    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        return new DatabaseCsrfTokenRepository(csrfTokenJpaRepository);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.csrfTokenRepository(csrfTokenRepository()))
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                );

        http.addFilterAfter(csrfHeaderWriterFilter, CsrfFilter.class);

        return http.build();
    }

    // 이렇게 Config 안에서도 작성 가능하지만 SRP 원칙에 따른다면 분리해야 한다.
    /*@Bean
    public Filter csrfTokenResponseHeaderBindingFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                    throws ServletException, IOException {
                CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
                if (csrfToken != null) {
                    response.setHeader("X-CSRF-TOKEN", csrfToken.getToken());
                }
                filterChain.doFilter(request, response);
            }
        };
    }*/
}