package com.springsecuritypractice.voter.security5;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;

import java.util.Collection;

public class CustomRoleVoter implements AccessDecisionVoter<Object> {

    private static final String ROLE_PREFIX = "ROLE_";

    @Override
    public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> attributes) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ACCESS_DENIED;
        }

        Collection<? extends GrantedAuthority> userAuthorities = authentication.getAuthorities();

        for (ConfigAttribute attribute : attributes) {
            if (!this.supports(attribute)) continue;

            String requiredRole = attribute.getAttribute();

            for (GrantedAuthority authority : userAuthorities) {
                if (requiredRole.equals(authority.getAuthority())) {
                    return ACCESS_GRANTED;
                }
            }
        }

        return ACCESS_DENIED;
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return attribute.getAttribute() != null &&
               attribute.getAttribute().startsWith(ROLE_PREFIX);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}