package com.sidematch.backend.config.jwt;

import com.sidematch.backend.domain.user.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.crypto.SecretKey;

@Getter
@NoArgsConstructor
@Entity
public class Jwt {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String jwtKey;

    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    public Jwt(User user) {
        SecretKey key = Jwts.SIG.HS256.key().build();
        this.jwtKey = Encoders.BASE64.encode(key.getEncoded());
        this.user = user;
    }
}
