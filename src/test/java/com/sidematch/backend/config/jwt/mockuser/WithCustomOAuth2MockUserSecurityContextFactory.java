package com.sidematch.backend.config.jwt.mockuser;

import com.sidematch.backend.config.oauth.userservice.CustomOAuth2User;
import com.sidematch.backend.domain.user.UserRole;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WithCustomOAuth2MockUserSecurityContextFactory implements WithSecurityContextFactory<WithCustomOAuth2MockUser> {

    @Override
    public SecurityContext createSecurityContext(WithCustomOAuth2MockUser mockUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        OAuth2User principal = getoAuth2User(mockUser);

        OAuth2AuthenticationToken token = new OAuth2AuthenticationToken(
                principal,
                principal.getAuthorities(),
                mockUser.registrationId());

        context.setAuthentication(token);
        return context;
    }

    private OAuth2User getoAuth2User(WithCustomOAuth2MockUser mockUser) {
        String nameAttributeKey = "sub";
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("username", mockUser.username());
        attributes.put("name", mockUser.name());
        attributes.put(nameAttributeKey, mockUser.registrationId());

        OAuth2User principal = new CustomOAuth2User(
                List.of(new SimpleGrantedAuthority(UserRole.USER.toString())),
                attributes,
                nameAttributeKey,
                mockUser.id(),
                mockUser.role());
        return principal;
    }
}
