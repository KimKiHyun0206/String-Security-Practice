package com.springsecuritypractice.voter.security6.manager;

import com.springsecuritypractice.voter.security6.CustomAuthorizationVoter;
import com.springsecuritypractice.voter.security6.policy.AuthorizationDecisionPolicy;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;


@RequiredArgsConstructor
public class CustomAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private final List<CustomAuthorizationVoter> voters;
    private final AuthorizationDecisionPolicy policy;

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
        HttpServletRequest request = context.getRequest();
        Authentication auth = authentication.get();

        List<Integer> votes = new ArrayList<>();
        for (CustomAuthorizationVoter voter : voters) {
            votes.add(voter.vote(auth, request));
        }

        return policy.decide(votes);
    }
}