package io.github.rahulrajsonu.securexai.service;

import io.github.rahulrajsonu.securexai.data.entity.NamespaceConfig;
import io.github.rahulrajsonu.securexai.data.entity.Permission;
import io.github.rahulrajsonu.securexai.data.repository.NamespaceConfigRepository;
import io.github.rahulrajsonu.securexai.data.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;

@Component
public class DefaultNamespaceConfigManager implements NamespaceConfigManager {
    private final NamespaceConfigRepository namespaceConfigRepository;
    private final PermissionRepository permissionRepository;

    @Autowired
    public DefaultNamespaceConfigManager(NamespaceConfigRepository namespaceConfigRepository,
                                         PermissionRepository permissionRepository) {
        this.namespaceConfigRepository = namespaceConfigRepository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public void addPermission(String namespace, String objectId, String relation, Set<String> users) {
        NamespaceConfig namespaceConfig = namespaceConfigRepository.findByNamespace(namespace);
        if (namespaceConfig == null) {
            namespaceConfig = new NamespaceConfig();
            namespaceConfig.setNamespace(namespace);
            namespaceConfig = namespaceConfigRepository.save(namespaceConfig);
        }
        Permission permission = new Permission();
        permission.setNamespaceConfig(namespaceConfig);
        permission.setObjectId(objectId);
        permission.setRelation(relation);
        permission.setUsers(users);
        permissionRepository.save(permission);
    }

    @Override
    public Set<String> getUsers(String namespace, String objectId, String relation) {
        NamespaceConfig namespaceConfig = namespaceConfigRepository.findByNamespace(namespace);
        if (namespaceConfig != null) {
            // You need to implement a method to fetch users by objectId and relation from permissionRepository
        }
        return Collections.emptySet();
    }

    @Override
    public NamespaceConfig getNamespaceConfig(String namespace) {
        return namespaceConfigRepository.findByNamespace(namespace);
    }
}
