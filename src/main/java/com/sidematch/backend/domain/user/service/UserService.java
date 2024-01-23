package com.sidematch.backend.domain.user.service;

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

    public User loadUserByEmailInOAuth2(final String email, String name) {
        Optional<User> user = getOptUser(email);
        return user.orElseGet(() -> singUp(email, name));
    }

    public User loadUserById(Long userId) {
        Optional<User> user = getOptUser(userId);
        return user.orElseThrow(() -> new IllegalArgumentException("등록되지 않은 정보입니다."));
    }

    private User singUp(String email, String name) {
        User newUser = User.builder()
                .email(email)
                .name(name)
                .build();
        userRepository.save(newUser);
        log.info("{} id를 가진 유저가 생성되었습니다.", newUser.getId());

        return newUser;
    }

    private Optional<User> getOptUser(String email) {
        if (email == null) {
            throw new IllegalArgumentException("비어있는 정보입니다.");
        }

        return userRepository.findUserByEmail(email);
    }

    private Optional<User> getOptUser(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("비어있는 정보입니다.");
        }

        return userRepository.findById(userId);
    }

}
