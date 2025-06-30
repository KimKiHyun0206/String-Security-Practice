package com.springsecuritypractice.student.domain.converter;

import com.springsecuritypractice.student.domain.vo.Password;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Converter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PasswordEncodeConverter implements AttributeConverter<String, String> {
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();    //순환 참조를 막기 위해서 주입함

    @Override
    public String convertToDatabaseColumn(String attribute) {
        var password = new Password(attribute);
        password.setEncodePassword(passwordEncoder.encode(attribute));

        return password.getPassword();
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return dbData;
    }
}