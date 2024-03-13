package com.sidematch.backend.domain.user.util;

import com.sidematch.backend.domain.user.User;
import org.springframework.security.core.Authentication;

import java.util.Optional;

public class UserUtil {

    public static Optional<User> getUser(Authentication authentication) {
        if (authentication == null) return Optional.empty();

        return Optional.of(
                (User) authentication.getPrincipal());
    }
}
