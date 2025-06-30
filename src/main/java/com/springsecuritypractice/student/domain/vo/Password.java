package com.springsecuritypractice.student.domain.vo;

import lombok.Getter;

@Getter
public class Password {
    //(최소 8글자, 글자 1개, 숫자 1개, 특수문자 1개)
    private static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";
    private String password;

    public Password(String password) {
        validatePassword(password);
    }

    private void validatePassword(String password) {
        this.password = password;
    }

    public void setEncodePassword(String encodePassword) {
        this.password = encodePassword;
    }
}