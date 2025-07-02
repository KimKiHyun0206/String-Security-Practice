package com.springsecuritypractice.voter.security6;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class IpAddressVoter implements CustomAuthorizationVoter {
    @Override
    public int vote(Authentication authentication, HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        if (ip.startsWith("192.168.")) {
            return ACCESS_GRANTED;
        }
        return ACCESS_DENIED;
    }
}