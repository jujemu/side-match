package com.sidematch.backend.config.jwt;

import com.sidematch.backend.config.jwt.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;

/**
 * 아래 구현에 혼동될 수 있는 단어를 정리
 * JwtKey: jwt 검증에 필요한 key: Key
 * JwtSecret: key 생성에 필요한 문자열: String
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class TokenProvider {

    private final JwtService jwtService;

    public final static String ISSUER = "side-match";
    public final static String HEADER_AUTHORIZATION = "Authorization";
    public final static String TOKEN_PREFIX = "Bearer";
    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14);
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofHours(2);

    public String generateToken(Long userId, String role, Duration expiredAt) {
        Date now = new Date();

        Jwt jwt = jwtService.createJwt(userId);
        String jwtSecret = jwt.getJwtSecret();
        return Jwts.builder()
                .issuer(ISSUER)
                .header().keyId(String.valueOf(jwt.getId())).and()
                .subject(String.valueOf(userId))
                .claim("role", role)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expiredAt.toMillis()))
                .signWith(getJwtKeyFromSecret(jwtSecret))
                .compact();
    }

    public Claims loadPayloadAndValidateToken(String token) throws JwtException{
        return Jwts.parser()
                .keyLocator(getKeyLocator())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private CustomKeyLocator getKeyLocator() {
        return new CustomKeyLocator(this.jwtService);
    }

    private SecretKey getJwtKeyFromSecret(String jwtSecret) {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }
}
