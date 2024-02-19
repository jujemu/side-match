package com.sidematch.backend.config.oauth;

import com.sidematch.backend.config.jwt.JwtProvider;
import com.sidematch.backend.config.oauth.userservice.CustomOAuth2User;
import com.sidematch.backend.domain.user.Role;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import static com.sidematch.backend.config.jwt.JwtProvider.HEADER_AUTHORIZATION;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
class OAuth2SuccessHandlerTest {

    @Autowired
    OAuth2SuccessHandler oAuth2SuccessHandler;

    @MockBean
    JwtProvider jwtProvider;

    @Mock
    HttpServletResponse response;

    @Mock
    HttpServletRequest request;

    @DisplayName("OAuth2.0 로그인 인증이 성공하면 사용자에게 jwt 발급하고 /login/success 리다이렉트한다.")
    @Test
    void onAuthenticationSuccess() throws Exception {
        //given
        OAuth2User oauth2User = getOauth2User();
        Authentication authentication = mock(Authentication.class);
        given(authentication.getPrincipal())
                .willReturn(oauth2User);

        String token = "test_token";
        given(jwtProvider.generateToken(anyLong(), anyString(), any(Duration.class)))
                .willReturn(token);

        //when
        oAuth2SuccessHandler.onAuthenticationSuccess(request, response, authentication);

        // then
        verify(response).setHeader(eq(HEADER_AUTHORIZATION), eq(token));
        verify(response).sendRedirect("/login/success");
    }

    private OAuth2User getOauth2User() {
        String nameAttributeKey = "sub";
        String registrationId = "test_reg";
        Long userId = 1L;
        String userRole = "test_role";

        return new CustomOAuth2User(
                List.of(new SimpleGrantedAuthority(Role.USER.toString())),
                Map.of(nameAttributeKey, registrationId),
                nameAttributeKey,
                userId,
                userRole
        );
    }
}