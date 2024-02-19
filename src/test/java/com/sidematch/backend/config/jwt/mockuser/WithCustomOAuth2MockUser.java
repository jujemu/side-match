package com.sidematch.backend.config.jwt.mockuser;


import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithCustomOAuth2MockUserSecurityContextFactory.class)
public @interface WithCustomOAuth2MockUser {

    long id() default 1L;
    String username() default "test@test.com";
    String name() default "test";
    String role() default "ROLE_USER";
    String registrationId() default "google";
}
