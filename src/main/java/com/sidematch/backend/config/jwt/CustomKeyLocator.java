package com.sidematch.backend.config.jwt;

import com.sidematch.backend.config.jwt.service.JwtService;
import io.jsonwebtoken.LocatorAdapter;
import io.jsonwebtoken.ProtectedHeader;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

import javax.crypto.SecretKey;
import java.security.Key;

@RequiredArgsConstructor
public class CustomKeyLocator extends LocatorAdapter<Key> {

    private final JwtService jwtService;

    @Override
    public Key locate(ProtectedHeader header) {
        String keyId = header.getKeyId();
        return lookupKey(keyId);
    }

    private SecretKey lookupKey(String keyId) {
        Jwt jwt = jwtService.loadJwtById(Long.parseLong(keyId));
        return getJwtKeyFromSecret(jwt.getJwtSecret());
    }

    private SecretKey getJwtKeyFromSecret(String jwtSecret) {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }
}