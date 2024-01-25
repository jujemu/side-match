package com.sidematch.backend.domain.user;

import com.sidematch.backend.config.jwt.Jwt;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;
    private String name;
    private String nickname;
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private Jwt jwt;

    @Builder
    private User(String email, Role role, String name, String nickname) {
        this.email = email;
        this.role = role != null ? role : Role.USER;
        this.name = name;
        this.nickname = nickname;
    }
}
