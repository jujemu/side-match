package com.sidematch.backend.config.jwt.service;

import com.sidematch.backend.config.jwt.Jwt;
import com.sidematch.backend.config.jwt.repository.JwtRepository;
import com.sidematch.backend.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class JwtService {

    private final JwtRepository jwtRepository;

    public Jwt createJwt(User user) {
        return jwtRepository.save(new Jwt(user));
    }

    public String loadJwtKeyByUserId(Long userId) {
        Jwt jwt = jwtRepository.findJwtByUserId(userId);
        return jwt.getJwtKey();
    }
}
