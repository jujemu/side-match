package com.sidematch.backend.config.oauth;

import com.sidematch.backend.config.jwt.JwtProvider;
import com.sidematch.backend.config.oauth.userservice.CustomOAuth2User;
import com.sidematch.backend.domain.user.User;
import com.sidematch.backend.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import static com.sidematch.backend.config.jwt.JwtProvider.ACCESS_TOKEN_DURATION;
import static com.sidematch.backend.config.jwt.JwtProvider.HEADER_AUTHORIZATION;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        super.clearAuthenticationAttributes(request);

        CustomOAuth2User auth2User = (CustomOAuth2User) authentication.getPrincipal();
        Long userId = auth2User.getUserId();
        User user = userService.loadUserById(userId);
        String role = auth2User.getRole();

        String generatedToken = jwtProvider.generateToken(user, role, ACCESS_TOKEN_DURATION);
        response.setHeader(HEADER_AUTHORIZATION, generatedToken);
    }

}
