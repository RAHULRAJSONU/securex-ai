package io.github.rahulrajsonu.securexai.service;

import io.github.rahulrajsonu.securexai.data.entity.Tenant;
import io.github.rahulrajsonu.securexai.data.model.AuthStrategy;
import io.github.rahulrajsonu.securexai.data.repository.TenantRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class TenantService {

    private final TenantRepository tenantRepository;

    private final BeanFactory beanFactory;

    public PasswordEncoder passwordEncoder(){
        return beanFactory.getBean(PasswordEncoder.class);
    }

    public Optional<Tenant> findByTenantId(String tenantId){
        return tenantRepository.findByTenantId(tenantId);
    }

    public Tenant register(Tenant tenant){
        tenant.setAuthStrategy(AuthStrategy.ApiKey);
        tenant.setApiKey(generateApiKey());
        var secret = generateSecretKey();
        tenant.setApiSecret(passwordEncoder().encode(secret));
        Tenant saved = tenantRepository.save(tenant);
        saved.setApiSecret(secret);
        return saved;
    }

    // Generate API Key
    private String generateApiKey() {
        // Generate a random byte array
        byte[] apiKeyBytes = new byte[16];
        new SecureRandom().nextBytes(apiKeyBytes);
        // Convert the byte array to a base64 string
        return Base64.getEncoder().encodeToString(apiKeyBytes);
    }

    // Generate Secret Key
    private String generateSecretKey() {
        // Generate a random byte array
        byte[] secretKeyBytes = new byte[32];
        new SecureRandom().nextBytes(secretKeyBytes);
        // Convert the byte array to a base64 string
        return Base64.getEncoder().encodeToString(secretKeyBytes);
    }
}
