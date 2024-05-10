package io.github.rahulrajsonu.securexai.security.provider;

import io.github.rahulrajsonu.securexai.exception.AuthException;
import io.github.rahulrajsonu.securexai.security.AuthProvider;
import org.springframework.stereotype.Component;

@Component
public interface JwtProvider extends AuthProvider {
    boolean verifyToken(String token) throws AuthException;
}
