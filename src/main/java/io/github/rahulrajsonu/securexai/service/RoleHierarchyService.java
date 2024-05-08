package io.github.rahulrajsonu.securexai.service;

import io.github.rahulrajsonu.securexai.data.entity.RoleHierarchy;
import io.github.rahulrajsonu.securexai.data.repository.NamespaceConfigRepository;
import io.github.rahulrajsonu.securexai.data.repository.PermissionRepository;
import io.github.rahulrajsonu.securexai.data.repository.RoleHierarchyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleHierarchyService {

    private final RoleHierarchyRepository repository;
    private final PermissionRepository permissionRepository;
    private final NamespaceConfigRepository namespaceConfigRepository;

    public Optional<RoleHierarchy> findActiveHierarchy(String namespace, String objectId) {
        return repository.findByNamespaceAndObjectIdAndIsActiveTrue(namespace, objectId);
    }

    @CacheEvict(value = "roleHierarchy", key = "#namespace + '-' + #objectId", allEntries = true)
    public RoleHierarchy save(RoleHierarchy roleHierarchy) {
        roleHierarchy.validate();
        if (namespaceConfigRepository.existsByNamespace(roleHierarchy.getNamespace()) &&
                permissionRepository.existsByObjectId(roleHierarchy.getObjectId())) {
            return repository.save(roleHierarchy);
        } else {
            throw new RuntimeException("Invalid namespace:objectId combination, "
                    + roleHierarchy.getNamespace() + ":" + roleHierarchy.getObjectId());
        }
    }

    @Cacheable(value = "roleHierarchy", key = "#namespace + '-' + #objectId")
    public RoleHierarchy getHierarchy(String namespace, String objectId) {
        return repository.findByNamespaceAndObjectId(namespace, objectId)
                .orElseThrow(() -> new RuntimeException("Role hierarchy not found for the namespace: " + namespace + " and objectId: " + objectId));
    }

    public boolean hasRelation(String namespace, String objectId, String userRole, String requiredRole) {
        Optional<RoleHierarchy> activeHierarchy = findActiveHierarchy(namespace, objectId);
        if (activeHierarchy.isEmpty()) {
            return false;
        }
        var hierarchy = activeHierarchy.get().getExpression();
        String[] roles = hierarchy.split(" > ");
        int userRoleIndex = Arrays.asList(roles).indexOf(userRole);
        int requiredPermissionIndex = Arrays.asList(roles).indexOf(requiredRole);
        return userRoleIndex <= requiredPermissionIndex;
    }

    public List<String> getInheritedRoles(String namespace, String objectId, String requiredRole){
        Optional<RoleHierarchy> activeHierarchy = findActiveHierarchy(namespace, objectId);
        if (activeHierarchy.isEmpty()) {
            return List.of();
        }
        var hierarchy = activeHierarchy.get().getExpression();
        String[] roles = hierarchy.split(" > ");
        int requiredPermissionIndex = Arrays.asList(roles).indexOf(requiredRole);
        return List.of(Arrays.copyOfRange(roles, 0, requiredPermissionIndex));
    }
}
