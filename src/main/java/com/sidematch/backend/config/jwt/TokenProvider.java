package com.sidematch.backend.config.jwt;

import com.sidematch.backend.domain.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.Password;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class TokenProvider {

    private final JwtProperties properties;

    public final static String HEADER_AUTHORIZATION = "Authorization";
    public final static String TOKEN_PRIFIX = "Bearer";
    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14);
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofHours(2);

    public String generateToken(Long userId, String role, Duration expiredAt) {
        Date now = new Date();

        return Jwts.builder()
                .issuer(properties.getIssuer())
                .subject(String.valueOf(userId))
                .claim("role", role)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expiredAt.toMillis()))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(properties.getKey())))
                .compact();
    }

    public Authentication getAuthentication(String authorizationHeader, User user) {
        String token = getToken(authorizationHeader);
        Password key = Keys.password(properties.getKey().toCharArray());
        Claims payload = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return new UsernamePasswordAuthenticationToken(
                user,
                null,
                List.of(new SimpleGrantedAuthority((String) payload.get("role")))
        );
    }

    public Long getUserId(String authorizationHeader) throws JwtException {
        String token = getToken(authorizationHeader);
        Password key = Keys.password(properties.getKey().toCharArray());
        Claims payload = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return Long.parseLong(payload.getSubject());
    }

    private String getToken(String authorizationHeader) {
        if (!authorizationHeader.startsWith(TOKEN_PRIFIX)) {
            throw new IllegalArgumentException("잘못된 인증 헤더 요청입니다.");
        }

        return authorizationHeader.substring(TOKEN_PRIFIX.length());
    }
}
