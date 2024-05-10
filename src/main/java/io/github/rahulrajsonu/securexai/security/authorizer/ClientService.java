package io.github.rahulrajsonu.securexai.security.authorizer;

import io.github.rahulrajsonu.securexai.data.entity.Client;
import io.github.rahulrajsonu.securexai.data.repository.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public void registerClient(String clientId, String clientSecret) {
        // Logic to register a client
    }

    public boolean isValidClient(String clientId, String clientSecret) {
        // Logic to validate client credentials
        return true; // Dummy implementation, replace with actual logic
    }

    public Optional<Client> getClient(String tenantId) {
        return clientRepository.findByTenantId(tenantId);
    }
}
