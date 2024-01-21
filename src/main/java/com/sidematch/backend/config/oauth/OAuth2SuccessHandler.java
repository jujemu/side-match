package com.sidematch.backend.config.oauth;

import com.sidematch.backend.config.jwt.TokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

import static com.sidematch.backend.config.jwt.TokenProvider.ACCESS_TOKEN_DURATION;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        super.clearAuthenticationAttributes(request);

        CustomOAuth2User auth2User = (CustomOAuth2User) authentication.getPrincipal();
        Long userId = auth2User.getUserId();

        String generatedToken = tokenProvider.generateToken(userId, ACCESS_TOKEN_DURATION);
        String targetUrl = getTargetUrl(generatedToken);

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private String getTargetUrl(String token) {
        return UriComponentsBuilder.fromUriString("/api/token")
                .queryParam("token", token)
                .build()
                .toUriString();
    }
}
