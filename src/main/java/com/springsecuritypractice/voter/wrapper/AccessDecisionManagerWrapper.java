package com.springsecuritypractice.voter.wrapper;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Supplier;

public class AccessDecisionManagerWrapper implements AuthorizationManager<RequestAuthorizationContext> {

    private final AccessDecisionManager accessDecisionManager;

    public AccessDecisionManagerWrapper(AccessDecisionManager accessDecisionManager) {
        this.accessDecisionManager = accessDecisionManager;
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
        try {
            String uri = context.getRequest().getRequestURI();
            Collection<ConfigAttribute> attributes = determineAttributesByUri(uri);
            accessDecisionManager.decide(authentication.get(), context, attributes);
            return new AuthorizationDecision(true);
        } catch (AccessDeniedException ex) {
            return new AuthorizationDecision(false);
        }
    }

    private Collection<ConfigAttribute> determineAttributesByUri(String uri) {
        if (uri.startsWith("/api/admin")) {
            return Collections.singletonList(() -> "ROLE_ADMIN");
        }
        return Collections.emptyList();
    }
}
