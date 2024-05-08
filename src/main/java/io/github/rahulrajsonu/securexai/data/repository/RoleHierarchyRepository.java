package io.github.rahulrajsonu.securexai.data.repository;

import io.github.rahulrajsonu.securexai.data.entity.RoleHierarchy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleHierarchyRepository extends JpaRepository<RoleHierarchy, Long> {
    Optional<RoleHierarchy> findByNamespaceAndObjectIdAndIsActiveTrue(String namespace, String objectId);

    Optional<RoleHierarchy> findByNamespaceAndObjectId(String namespace, String objectId);

    boolean existsByNamespaceAndObjectId(String namespace, String objectId);
}
