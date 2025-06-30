package com.springsecuritypractice.student.dto.response;

import com.springsecuritypractice.auth.domain.Authority;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Set;

@Builder
@ToString
@Getter
public class StudentResponse {
    private Long id;
    private String loginId;
    private String password;
    private String name;
    private Set<Authority> authorities;
}