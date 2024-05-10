package io.github.rahulrajsonu.securexai.data.repository;

import io.github.rahulrajsonu.securexai.data.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {
    Optional<Client> findByTenantId(String tenantId);
}
