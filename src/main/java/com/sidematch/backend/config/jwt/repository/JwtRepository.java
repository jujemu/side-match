package com.sidematch.backend.config.jwt.repository;

import com.sidematch.backend.config.jwt.Jwt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JwtRepository extends JpaRepository<Jwt, Long> {

    Jwt findJwtByUserId(Long userId);
}
