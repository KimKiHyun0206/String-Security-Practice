package com.springsecuritypractice.login.dto;

import lombok.Getter;

@Getter
public class LoginRequest {
    private String loginId;
    private String password;
}
