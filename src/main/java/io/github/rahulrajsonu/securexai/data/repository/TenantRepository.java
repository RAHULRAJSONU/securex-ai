package io.github.rahulrajsonu.securexai.data.repository;

import io.github.rahulrajsonu.securexai.data.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TenantRepository extends JpaRepository<Tenant, String> {
    Optional<Tenant> findByTenantId(String tenantId);
}
