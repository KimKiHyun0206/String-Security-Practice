package com.springsecuritypractice.voter.security6.policy;

import com.springsecuritypractice.voter.security6.CustomAuthorizationVoter;
import org.springframework.security.authorization.AuthorizationDecision;

import java.util.List;

public class UnanimousPolicy implements AuthorizationDecisionPolicy {
    @Override
    public AuthorizationDecision decide(List<Integer> votes) {
        if (votes.contains(CustomAuthorizationVoter.ACCESS_DENIED)) {
            return new AuthorizationDecision(false);
        }
        // 찬성만 있고 기권도 있으면 거부 (전원 찬성만 허용)
        boolean allGrantOrAbstain = votes.stream()
            .allMatch(v -> v == CustomAuthorizationVoter.ACCESS_GRANTED || v == CustomAuthorizationVoter.ACCESS_ABSTAIN);

        if (allGrantOrAbstain && votes.contains(CustomAuthorizationVoter.ACCESS_GRANTED)) {
            return new AuthorizationDecision(true);
        }
        return new AuthorizationDecision(false);
    }
}