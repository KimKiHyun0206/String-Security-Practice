package com.springsecuritypractice.voter.security6.policy;

import com.springsecuritypractice.voter.security6.CustomAuthorizationVoter;
import org.springframework.security.authorization.AuthorizationDecision;

import java.util.List;

public class ConsensusPolicy implements AuthorizationDecisionPolicy {
    @Override
    public AuthorizationDecision decide(List<Integer> votes) {
        long grantCount = votes.stream()
                .filter(v -> v == CustomAuthorizationVoter.ACCESS_GRANTED)
                .count();

        long denyCount = votes.stream()
                .filter(v -> v == CustomAuthorizationVoter.ACCESS_DENIED)
                .count();

        if (grantCount > denyCount) {
            return new AuthorizationDecision(true);
        }
        return new AuthorizationDecision(false);
    }
}