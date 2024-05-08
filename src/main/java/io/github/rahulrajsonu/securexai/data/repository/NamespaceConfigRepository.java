package io.github.rahulrajsonu.securexai.data.repository;

import io.github.rahulrajsonu.securexai.data.entity.NamespaceConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NamespaceConfigRepository extends JpaRepository<NamespaceConfig, Long> {
    NamespaceConfig findByNamespace(String namespace);

    boolean existsByNamespace(String namespace);
}