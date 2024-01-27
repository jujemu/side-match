package com.sidematch.backend.config.jwt.repository;

import com.sidematch.backend.config.jwt.Jwt;
import com.sidematch.backend.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JwtRepository extends JpaRepository<Jwt, Long> {

    Optional<Jwt> findByUser(User user);
}
