package com.sidematch.backend.config.oauth;

import com.sidematch.backend.domain.user.User;
import com.sidematch.backend.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        final OAuth2User oAuth2User = super.loadUser(userRequest);

        final String registrationId = userRequest.getClientRegistration().getRegistrationId();
        final OAuthAttributes attributes = OAuthAttributes.of(registrationId, oAuth2User.getAttributes());
        final User user = userService.loadUserByEmailInLogin(attributes);

        return new CustomOAuth2User(
                List.of(new SimpleGrantedAuthority(user.getRole().toString())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey(),
                user.getId()
        );
    }
}
