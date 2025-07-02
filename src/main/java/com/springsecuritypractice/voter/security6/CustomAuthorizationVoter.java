package com.springsecuritypractice.voter.security6;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface CustomAuthorizationVoter {
    int ACCESS_GRANTED = 1;
    int ACCESS_DENIED = -1;
    int ACCESS_ABSTAIN = 0;

    int vote(Authentication authentication, HttpServletRequest request);
}
