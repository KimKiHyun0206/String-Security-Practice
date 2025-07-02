package com.springsecuritypractice.config.voter;

import com.springsecuritypractice.jwt.JwtFilter;
import com.springsecuritypractice.voter.security5.CustomIpCheckVoter;
import com.springsecuritypractice.voter.security5.CustomRoleVoter;
import com.springsecuritypractice.voter.security5.wrapper.AccessDecisionManagerWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import java.util.List;

@EnableWebSecurity
@RequiredArgsConstructor
public class Security5VoterConfig {
    private final JwtFilter jwtFilter;

    @Bean
    public CustomIpCheckVoter ipCheckVoter() {
        return new CustomIpCheckVoter();
    }

    @Bean
    public CustomRoleVoter roleVoter() {
        return new CustomRoleVoter();
    }

    /*
     * UnanimousBased: 전원 찬성해야 허용
     * ConsensusBased: 찬성 다수면 허용
     * AffirmativeBased: 하나라도 허용하면 허용
     * */
    @Bean
    public AccessDecisionManager accessDecisionManager() {
        // UnanimousBased, AffirmativeBased, ConsensusBased 중 선택
        return new UnanimousBased(List.of(ipCheckVoter(), roleVoter()));
    }

    @Bean
    public AuthorizationManager<RequestAuthorizationContext> accessDecisionManagerWrapper() {
        // 5버전 AccessDecisionManager를 6버전 AuthorizationManager로 래핑
        return new AccessDecisionManagerWrapper(accessDecisionManager());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/**",
                                "/swagger-ui/**",
                                "/api-docs/**",
                                "/swagger-ui.html",
                                "/login",
                                "/login/**",
                                "/**"
                        ).permitAll()
                        // 여기서 wrapper로 Voter 기반 인가 처리 연결
                        .requestMatchers("/api/admin/**").access(accessDecisionManagerWrapper())
                        .anyRequest().authenticated()
                )
                // JWT 필터 등록
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}