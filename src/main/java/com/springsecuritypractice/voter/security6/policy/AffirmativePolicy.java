package com.springsecuritypractice.voter.security6.policy;

import com.springsecuritypractice.voter.security6.CustomAuthorizationVoter;
import org.springframework.security.authorization.AuthorizationDecision;

import java.util.List;

public class AffirmativePolicy implements AuthorizationDecisionPolicy {
    @Override
    public AuthorizationDecision decide(List<Integer> votes) {
        if (votes.contains(CustomAuthorizationVoter.ACCESS_GRANTED)) {
            return new AuthorizationDecision(true);
        }
        if (votes.contains(CustomAuthorizationVoter.ACCESS_DENIED)) {
            return new AuthorizationDecision(false);
        }
        return new AuthorizationDecision(false);
    }
}