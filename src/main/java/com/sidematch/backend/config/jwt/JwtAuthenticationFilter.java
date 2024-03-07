package com.sidematch.backend.config.jwt;

import com.sidematch.backend.domain.user.User;
import com.sidematch.backend.domain.user.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import static com.sidematch.backend.config.jwt.JwtProvider.HEADER_AUTHORIZATION;
import static com.sidematch.backend.config.jwt.JwtProvider.TOKEN_PREFIX;
import static org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserService userService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = setTokenInRequestAttribute(request);
            Claims payload = jwtProvider.loadPayloadAndValidateToken(token);

            Long userId = Long.parseLong(payload.getSubject());
            User user = getUser(userId);
            Authentication authentication = getAuthentication(user);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (JwtException jwtException) {
            log.info("잘못된 토큰으로 접근했습니다.");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 토큰입니다.");
        } catch (IllegalArgumentException iAE) {
            filterChain.doFilter(request, response);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath().startsWith("/api/login") ||
                request.getServletPath().startsWith(DEFAULT_AUTHORIZATION_REQUEST_BASE_URI);
    }

    private String setTokenInRequestAttribute(HttpServletRequest request) throws IllegalArgumentException {
        String token = getToken(request);
        request.setAttribute("token", token);
        return token;
    }

    private String getToken(HttpServletRequest request) throws IllegalArgumentException {
        String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION);
        if (authorizationHeader == null || authorizationHeader.isEmpty()) {
            throw new IllegalArgumentException("토큰 없이 접근하고 있습니다.");
        }

        if (!authorizationHeader.startsWith(TOKEN_PREFIX)) {
            throw new IllegalArgumentException("잘못된 인증 헤더 요청입니다.");
        }

        return authorizationHeader.substring(TOKEN_PREFIX.length());
    }

    private User getUser(Long userId) {
        return userService.loadUserById(userId);
    }

    private Authentication getAuthentication(User user) {
        return new UsernamePasswordAuthenticationToken(
                user, null, List.of(new SimpleGrantedAuthority(user.getRole().toString())));
    }
}
