package com.springsecuritypractice.student.dto.request;

import lombok.Getter;

@Getter
public class StudentCreateRequest {
    private String loginId;
    private String password;
    private String name;
}
