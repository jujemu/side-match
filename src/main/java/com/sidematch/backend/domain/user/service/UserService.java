package com.sidematch.backend.domain.user.service;

import com.sidematch.backend.config.oauth.OAuthAttributes;
import com.sidematch.backend.domain.user.User;
import com.sidematch.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;

    public User loadUserByEmailInLogin(final OAuthAttributes attributes) {
        String email = attributes.getEmail();
        String name = attributes.getName();

        if (email == null) {
            throw new IllegalArgumentException("Invalid email");
        }

        Optional<User> user = userRepository.findUserByEmail(email);
        return user.orElseGet(() -> singUp(email, name));

    }

    private User singUp(String email, String name) {
        User newUser = User.builder()
                .email(email)
                .name(name)
                .build();
        userRepository.save(newUser);
        log.info("User with id {} is registered", newUser.getId());

        return newUser;
    }
}
