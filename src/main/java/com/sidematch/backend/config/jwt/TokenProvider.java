package com.sidematch.backend.config.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Component
public class TokenProvider {

    private final JwtProperties properties;

    public final static String HEADER_AUTHORIZATION = "Authorization";
    public final static String TOKEN_PRIFIX = "Bearer ";
    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14);
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofHours(2);

    public String generateToken(Long userId, Duration expiredAt) {
        Date now = new Date();

        return Jwts.builder()
                .issuer(properties.getIssuer())
                .subject(String.valueOf(userId))
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expiredAt.toMillis()))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(properties.getKey())))
                .compact();
    }

}
