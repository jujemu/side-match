package com.sidematch.backend.config.oauth;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {

    private final Map<String, Object> attributes;
    private final String name;
    private final String nameAttributeKey;
    private final String email;

    @Builder
    private OAuthAttributes(Map<String, Object> attributes, String name, String nameAttributeKey, String email) {
        this.attributes = attributes;
        this.name = name;
        this.nameAttributeKey = nameAttributeKey;
        this.email = email;
    }

    public static OAuthAttributes of(String registrationId, Map<String, Object> attributes) {
        return ofGoogle(attributes);
    }

    private static OAuthAttributes ofGoogle(Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .email((String) attributes.get("email"))
                .name((String) attributes.get("name"))
                .nameAttributeKey("sub")
                .attributes(attributes)
                .build();
    }
}
