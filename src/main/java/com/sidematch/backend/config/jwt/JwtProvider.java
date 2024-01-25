package com.sidematch.backend.config.jwt;

import com.sidematch.backend.config.jwt.service.JwtService;
import com.sidematch.backend.domain.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtProvider {

    private final JwtService jwtService;

    public final static String ISSUER = "side-match";
    public final static String HEADER_AUTHORIZATION = "Authorization";
    public final static String TOKEN_PRIFIX = "Bearer";
    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14);
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofHours(2);

    public String generateToken(User user, String role, Duration expiredAt) {
        Date now = new Date();

        Jwt jwt = jwtService.createJwt(user);

        return Jwts.builder()
                .issuer(ISSUER)
                .subject(String.valueOf(user.getId()))
                .id(String.valueOf(jwt.getId()))
                .claim("role", role)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expiredAt.toMillis()))
                .signWith(getKey(jwt.getJwtKey()))
                .compact();
    }

    public Authentication getAuthentication(String authorizationHeader, User user) {
        String token = getToken(authorizationHeader);
        Long userId = getUserId(authorizationHeader);
        String jwtKey = jwtService.loadJwtKeyByUserId(userId);
        Claims payload = Jwts.parser()
                .verifyWith(getKey(jwtKey))
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
        String jwtKey = getTokenKey(token);
        Claims payload = Jwts.parser()
                .verifyWith(getKey(jwtKey))
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

    private String getTokenKey(String token) {
        return Jwts.parser().build().parseSignedClaims(token).getPayload().getId();
    }

    private SecretKey getKey(String jwtKey) {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtKey));
    }
}
