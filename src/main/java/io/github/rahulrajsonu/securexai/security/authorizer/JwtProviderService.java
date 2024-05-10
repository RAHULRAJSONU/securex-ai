package io.github.rahulrajsonu.securexai.security.authorizer;

import io.github.rahulrajsonu.securexai.security.provider.DefaultJwtProvider;
import io.github.rahulrajsonu.securexai.security.provider.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class JwtProviderService {

    private static final String DEFAULT = "DEFAULT";
    private final Map<String, JwtProvider> providers = new HashMap<>();

    @Autowired
    public JwtProviderService(DefaultJwtProvider defaultProvider) {
        addProvider(DEFAULT, defaultProvider);
    }

    public void addProvider(String name, JwtProvider provider) {
        providers.put(name, provider);
    }

    public JwtProvider getProvider(String name) {
        return providers.getOrDefault(name, providers.get(DEFAULT));
    }

}
