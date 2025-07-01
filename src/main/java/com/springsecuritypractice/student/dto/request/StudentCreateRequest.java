package com.springsecuritypractice.student.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StudentCreateRequest {
    private String loginId;
    private String password;
    private String name;
}
