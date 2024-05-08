package io.github.rahulrajsonu.securexai.service;

import io.github.rahulrajsonu.securexai.data.entity.NamespaceConfig;
import io.github.rahulrajsonu.securexai.data.entity.Permission;
import io.github.rahulrajsonu.securexai.data.repository.NamespaceConfigRepository;
import io.github.rahulrajsonu.securexai.data.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionRepository permissionRepository;
    private final NamespaceConfigRepository namespaceConfigRepository;

    public void upsert(String objectId, String relation, String namespace, Set<String> users) {
        NamespaceConfig namespaceConfig = namespaceConfigRepository.findByNamespace(namespace);
        if (namespaceConfig == null) {
            namespaceConfig = new NamespaceConfig();
            namespaceConfig.setNamespace(namespace);
            namespaceConfig = namespaceConfigRepository.save(namespaceConfig);
        }
        Permission existingPermission = permissionRepository.findByObjectIdAndRelationAndNamespaceConfigNamespace(objectId, relation, namespace);

        if (existingPermission != null) {
            Set<String> existingUsers = existingPermission.getUsers();
            existingUsers.addAll(users);
            existingPermission.setUsers(existingUsers);
            permissionRepository.save(existingPermission);
        } else {
            Permission newPermission = new Permission();
            newPermission.setObjectId(objectId);
            newPermission.setRelation(relation);
            newPermission.setNamespaceConfig(namespaceConfig);
            newPermission.setUsers(users);
            permissionRepository.save(newPermission);
        }
    }
}
