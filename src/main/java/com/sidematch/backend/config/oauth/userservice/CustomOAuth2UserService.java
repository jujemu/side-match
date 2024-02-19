package com.sidematch.backend.config.oauth.userservice;

import com.sidematch.backend.config.oauth.OAuth2UserInfoDto;
import com.sidematch.backend.domain.user.Role;
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
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        final OAuth2User oAuth2User = super.loadUser(userRequest);

        final String registrationId = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        final OAuth2UserInfoDto oAuth2UserInfoDto = OAuth2UserInfoDto.of(registrationId, attributes);

        Optional<User> optUser = userService.loadOptUserByEmail(oAuth2UserInfoDto.getEmail());
        if (optUser.isPresent()) {
            return getCustomOAuth2User(oAuth2UserInfoDto, optUser.get());
        }

        User user = userService.signUp(
                oAuth2UserInfoDto.getEmail(),
                oAuth2UserInfoDto.getName());
        return getCustomOAuth2User(oAuth2UserInfoDto, user);
    }

    private CustomOAuth2User getCustomOAuth2User(OAuth2UserInfoDto oAuth2UserInfoDto, User user) {
        return new CustomOAuth2User(
                List.of(new SimpleGrantedAuthority(Role.USER.toString())),
                oAuth2UserInfoDto.getAttributes(),
                oAuth2UserInfoDto.getNameAttributeKey(),
                user.getId(),
                user.getRole().toString());
    }
}
