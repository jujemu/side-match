package com.sidematch.backend.domain.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserTest {

    @DisplayName("회원가입을 하면 이메일과 User 역할을 갖는다.")
    @Test
    void createUser() throws Exception {
        //given
        String email = "test@test.com";

        //when
        User user = User.builder()
                .email(email)
                .build();

        //then
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getRole()).isEqualByComparingTo(UserRole.USER);
    }
}