package com.sidematch.backend.config.jwt.service;

import com.sidematch.backend.config.jwt.Jwt;
import com.sidematch.backend.config.jwt.repository.JwtRepository;
import com.sidematch.backend.domain.user.User;
import com.sidematch.backend.domain.user.service.UserService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class JwtService {

    private final JwtRepository jwtRepository;
    private final UserService userService;

    public Jwt createJwt(Long userId) {
        User user = userService.loadUserById(userId);
        return jwtRepository.save(new Jwt(user));
    }

    public String loadJwtSecret(Long tokenId) {
        Jwt jwt = jwtRepository.findById(tokenId).orElseThrow(() -> new IllegalArgumentException("유효하지 않은 토큰입니다."));
        return jwt.getJwtSecret();
    }

    public Jwt loadJwtById(Long jwtId) {
        return jwtRepository.findById(jwtId)
                .orElseThrow(() -> new JwtException("유효하지 않은 토큰입니다."));
    }
}
