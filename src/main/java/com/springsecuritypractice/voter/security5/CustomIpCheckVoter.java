package com.springsecuritypractice.voter.security5;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;

import java.util.Collection;

@Slf4j
public class CustomIpCheckVoter implements AccessDecisionVoter<Object> {

    @Override
    public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> attributes) {
        // IP 체크 예시
        FilterInvocation fi = (FilterInvocation) object;
        String ip = fi.getHttpRequest().getRemoteAddr();

        // IPv4 + IPv6 로컬 호스트 체크 (도커 컨테이너라면 172 대역이 나올 수 있음)
        if ("127.0.0.1".equals(ip) || "::1".equals(ip)) {
            log.info("[IpCheckVoter] 요청 IP: {}", ip);
            return ACCESS_GRANTED;
        }

        return ACCESS_DENIED;
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        // 특정 ConfigAttribute만 처리하고 싶을 때 조건 지정
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}