package io.github.rahulrajsonu.securexai.security;

import io.github.rahulrajsonu.securexai.exception.AuthException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Objects;

@Service
public class JwtService {

    public Jwt parse(String token, String issUri){
        JwtDecoder decoder = NimbusJwtDecoder.withJwkSetUri(issUri+".well-known/jwks.json").build();
        Jwt jwt = decoder.decode(token);
        boolean nonExpired = Objects.requireNonNull(jwt.getExpiresAt()).isBefore(Instant.now());
        if(nonExpired){
            return jwt;
        }
        throw new AuthException("Invalid bearer token");
    }

}