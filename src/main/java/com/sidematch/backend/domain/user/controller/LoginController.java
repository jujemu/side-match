package com.sidematch.backend.domain.user.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI;

@RestController
public class LoginController {

    @GetMapping("/api/login")
    public void login(HttpServletResponse response, @RequestParam String provider) throws IOException {
        String redirectUrl =  DEFAULT_AUTHORIZATION_REQUEST_BASE_URI + "/" + provider;
        response.sendRedirect(redirectUrl);
    }
}
