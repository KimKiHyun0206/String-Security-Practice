package com.springsecuritypractice.voter.security6.policy;

import org.springframework.security.authorization.AuthorizationDecision;

import java.util.List;

public interface AuthorizationDecisionPolicy {
    AuthorizationDecision decide(List<Integer> votes);
}