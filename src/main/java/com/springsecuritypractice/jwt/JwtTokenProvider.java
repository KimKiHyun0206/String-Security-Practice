package com.springsecuritypractice.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 토큰 발급, 유효성 검증, 토큰에서 Authentication 객체를 가져오는 역할을 하는 클래스
 *
 * @author duskafka
 */
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final String secretKey;
    private final long VALIDATE_IN_MILLISECONDS = 3600000; // 1시간

    public String createToken(Authentication authentication) {
        Claims claims = Jwts.claims().setSubject(authentication.getName());

        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        claims.put("auth", roles);

        Date now = new Date();
        Date expiry = new Date(now.getTime() + VALIDATE_IN_MILLISECONDS);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        String username = claims.getSubject();

        @SuppressWarnings("unchecked")
        List<String> roles = (List<String>) claims.get("auth");

        List<GrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // 여기서 UserDetails를 생성
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                username,
                "", // password는 필요 없으므로 빈 문자열
                authorities
        );

        return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
    }


    public boolean validateToken(String token) {
        try {
            getClaims(token); // 파싱되면 유효
            return true;
        } catch (Exception e) {
            log.warn("[JwtTokenProvider] Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }
}