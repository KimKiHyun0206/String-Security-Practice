package com.springsecuritypractice.config.filter;

import com.springsecuritypractice.filter.AdminFilter;
import com.springsecuritypractice.jwt.JwtFilter;
import com.springsecuritypractice.voter.wrapper.AccessDecisionManagerWrapper;
import com.springsecuritypractice.voter.CustomIpCheckVoter;
import com.springsecuritypractice.voter.CustomRoleVoter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
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
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.List;

@EnableWebSecurity
@RequiredArgsConstructor
public class JwtCustomFilterAndVoterConfig {
    private final JwtFilter jwtFilter;

    @Bean
    public OncePerRequestFilter myCustomFilter() {
        return new AdminFilter();
    }

    @Bean
    public CustomIpCheckVoter ipCheckVoter() {
        return new CustomIpCheckVoter();
    }

    @Bean
    public CustomRoleVoter roleVoter() {
        return new CustomRoleVoter();
    }

        /**
     * * UnanimousBased: 전원 찬성해야 허용
     *
     * <br>
     *
     * * ConsensusBased: 찬성 다수면 허용
     *
     * <br>
     *
     * * AffirmativeBased: 하나라도 허용하면 허용
     * */
    @Bean
    public AccessDecisionManager  accessDecisionManager() {
        return new UnanimousBased(List.of(
                ipCheckVoter(),
                roleVoter()
        ));
    }

    @Bean
    public AuthorizationManager<RequestAuthorizationContext> accessDecisionManagerWrapper() {
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
                        .requestMatchers("/api/admin/**").access(accessDecisionManagerWrapper())
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        // 커스텀 필터 등록 (UsernamePasswordAuthenticationFilter 앞에 삽입)
        http.addFilterBefore(myCustomFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}