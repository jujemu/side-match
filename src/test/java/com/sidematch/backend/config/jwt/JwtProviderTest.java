package com.sidematch.backend.config.jwt;

import com.sidematch.backend.config.jwt.service.JwtService;
import com.sidematch.backend.domain.user.User;
import com.sidematch.backend.domain.user.service.UserService;
import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.sidematch.backend.config.jwt.JwtProvider.ACCESS_TOKEN_DURATION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@Transactional
@SpringBootTest
class JwtProviderTest {

    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    JwtService jwtService;
    @Autowired
    UserService userService;

    @DisplayName("사용자의 고유한 access token 생성한다. 생성된 토큰에는 jwt id, sub(userId), 만료기간을 포함한다.")
    @Test
    void generateToken() throws Exception {
        //given
        String email = "test@test.com";
        String name = "test";
        User user = userService.signUp(email, name);

        //when
        String token = jwtProvider.generateToken(user.getId(), user.getRole().toString(), ACCESS_TOKEN_DURATION);

        //then
        Claims claims = jwtProvider.loadPayloadAndValidateToken(token);
        Long userIdFromToken = Long.parseLong(claims.getSubject());

        assertThat(userIdFromToken).isEqualTo(user.getId());
        assertThat(claims.get("role")).isEqualTo(user.getRole().toString());
    }

    @DisplayName("발급된 토큰으로 사용자가 요청하면 토큰을 검증할 수 있다.")
    @Test
    void validateToken() throws Exception {
        //given
        String email = "test@test.com";
        String name = "test";
        User user = userService.signUp(email, name);
        String token = jwtProvider.generateToken(user.getId(), user.getRole().toString(), ACCESS_TOKEN_DURATION);

        //when //then
        assertThatCode(() ->
                jwtProvider.loadPayloadAndValidateToken(token)).doesNotThrowAnyException();
    }

}