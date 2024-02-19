package com.sidematch.backend.config.oauth;

import com.sidematch.backend.config.jwt.JwtAuthenticationFilter;
import com.sidematch.backend.config.oauth.userservice.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class OAuth2LoginConfig {

    private final CustomOAuth2UserService oAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.oauth2Login(oauth2Login ->
                oauth2Login
                        .successHandler(oAuth2SuccessHandler)
                        .userInfoEndpoint(userInfoEndpointConfig ->
                                userInfoEndpointConfig.userService(oAuth2UserService)));

        http.sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterAfter(jwtAuthenticationFilter, OAuth2LoginAuthenticationFilter.class);

        // 로컬 환경에서는 서버 사이드로 구현하기 때문에 일부 엔드포인트 개방
        http.authorizeHttpRequests(a ->
                a.requestMatchers("/login/success").permitAll());

        http.authorizeHttpRequests(a ->
                a.requestMatchers("/error").permitAll()
                        .requestMatchers("/api/login").permitAll()
                        .requestMatchers("/login/success").permitAll()
                        .anyRequest().authenticated());

        return http.build();
    }
}
