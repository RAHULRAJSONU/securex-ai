package io.github.rahulrajsonu.securexai.security.authorizer;

import io.github.rahulrajsonu.securexai.security.provider.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JwtAuthorizerService {

    private final JwtProviderService jwtProviderService;
    private final ClientService clientService;

    @Autowired
    public JwtAuthorizerService(JwtProviderService jwtProviderService, ClientService clientService) {
        this.jwtProviderService = jwtProviderService;
        this.clientService = clientService;
    }

    public boolean authorize(String clientId, String clientSecret, String token, String providerName) {
        if (clientService.isValidClient(clientId, clientSecret)) {
            JwtProvider provider = jwtProviderService.getProvider(providerName);
            if (provider != null) {
                return provider.verifyToken(token);
            } else {
                throw new IllegalArgumentException("Invalid JWT provider name");
            }
        } else {
            return false;
        }
    }

}
