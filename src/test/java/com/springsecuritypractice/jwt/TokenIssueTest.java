package com.springsecuritypractice.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springsecuritypractice.login.dto.LoginRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class TokenIssueTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final String LOGIN_URL_PATH = "/api/jwt/login";

    @Test
    @DisplayName("로그인_성공시_헤더에_JWT_토큰이_담긴다")
    void loginSuccess_withHeader() throws Exception {
        LoginRequest request = new LoginRequest("admin", "admin");

        MvcResult result = mockMvc.perform(post(LOGIN_URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(header().exists("Authorization"))
                .andReturn();

        // then
        String authorizationHeader = result.getResponse().getHeader("Authorization");
        assertThat(authorizationHeader).startsWith("Bearer ");
        log.info("Authorization Hedaer: Bearer {}", authorizationHeader);
    }
}