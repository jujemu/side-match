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
    private String jwtSecret;

    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    public Jwt(User user) {
        SecretKey randomKey = Jwts.SIG.HS256.key().build();
        this.jwtSecret = Encoders.BASE64.encode(randomKey.getEncoded());
        this.user = user;
    }
}
