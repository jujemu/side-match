package com.sidematch.backend.config.oauth;

import com.sidematch.backend.config.jwt.TokenProvider;
import com.sidematch.backend.config.oauth.userservice.CustomOAuth2User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.sidematch.backend.config.jwt.TokenProvider.ACCESS_TOKEN_DURATION;
import static com.sidematch.backend.config.jwt.TokenProvider.HEADER_AUTHORIZATION;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        super.clearAuthenticationAttributes(request);

        CustomOAuth2User auth2User = (CustomOAuth2User) authentication.getPrincipal();
        Long userId = auth2User.getUserId();
        String role = auth2User.getRole();

        String generatedToken = tokenProvider.generateToken(userId, role, ACCESS_TOKEN_DURATION);
        response.setHeader(HEADER_AUTHORIZATION, generatedToken);
        response.sendRedirect("/login/success");
    }
}
