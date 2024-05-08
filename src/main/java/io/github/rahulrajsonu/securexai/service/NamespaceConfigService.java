package io.github.rahulrajsonu.securexai.service;

import io.github.rahulrajsonu.securexai.data.entity.NamespaceConfig;
import io.github.rahulrajsonu.securexai.data.entity.Permission;
import io.github.rahulrajsonu.securexai.data.repository.NamespaceConfigRepository;
import io.github.rahulrajsonu.securexai.data.repository.PermissionRepository;
import io.github.rahulrajsonu.securexai.parser.Attribute;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class NamespaceConfigService {
    private final NamespaceConfigRepository namespaceConfigRepository;
    private final PermissionRepository permissionRepository;
    private final PermissionService permissionService;
    private final RoleHierarchyService roleHierarchyService;

    public NamespaceConfigService(NamespaceConfigRepository namespaceConfigRepository,
                                  PermissionRepository permissionRepository, PermissionService permissionService, RoleHierarchyService roleHierarchyService) {
        this.namespaceConfigRepository = namespaceConfigRepository;
        this.permissionRepository = permissionRepository;
        this.permissionService = permissionService;
        this.roleHierarchyService = roleHierarchyService;
    }

    @CacheEvict(value = "permissions", allEntries = true)
    public void addPermission(String namespace, String objectId, String relation, Set<String> users) {
        permissionService.upsert(objectId,relation,namespace,users);
    }

    @Cacheable(value = "permissions", key = "#namespace + '-' + #objectId + '-' + #relation")
    public Set<String> getUsers(String namespace, String objectId, String relation) {
        log.info("Getting users for, namespace: {}, objectId: {}, and relation: {}", namespace, objectId, relation);
        NamespaceConfig byNamespace = this.namespaceConfigRepository.findByNamespace(namespace);
        Set<Permission> permissions = new HashSet<>();
        if (Attribute.ALL.equals(objectId)) {
            permissions = this.permissionRepository.findByNamespaceConfigAndRelation(byNamespace, relation);
        } else {
            permissions = this.permissionRepository.findByNamespaceConfigAndObjectIdAndRelation(byNamespace, objectId, relation);
        }
        return permissions.stream().map(Permission::getUsers).flatMap(Collection::stream).collect(Collectors.toSet());
    }

    public NamespaceConfig getNamespaceConfig(String namespace) {
        return namespaceConfigRepository.findByNamespace(namespace);
    }

    public boolean checkRoleHierarchy(String namespace, String objectId, String user, String requiredRole) {
        NamespaceConfig byNamespace = this.namespaceConfigRepository.findByNamespace(namespace);
        if (byNamespace.isRoleHierarchyEnabled()) {
            List<String> relations = getRelation(namespace, objectId, user);
            var hasAccess = false;
            for (String relation : relations) {
                if (roleHierarchyService.hasRelation(namespace, objectId, relation, requiredRole)) {
                    hasAccess = true;
                    break;
                }
            }
            return hasAccess || checkInheritance(namespace, objectId, requiredRole, user);
        } else {
            return false;
        }
    }

    private boolean checkInheritance(String namespace, String objectId, String requiredRole, String user) {
        List<String> inheritedRoles = roleHierarchyService.getInheritedRoles(namespace, objectId, requiredRole);
        boolean related = false;
        if (inheritedRoles.isEmpty()) {
            return false;
        }
        List<Permission> permissions = getRelation(namespace, objectId, inheritedRoles);
        for (Permission permission : permissions) {
            if (permission.getUsers().contains(user)) {
                related = true;
                break;
            }
            List<String> possibleNesting = permission.getUsers().stream().filter(u -> !u.startsWith("user:")).toList();
            for (String possible : possibleNesting) {
                String[] objectRelation = possible.split("#");
                if (objectRelation.length != 2) {
                    continue;
                }
                String[] namespaceObject = objectRelation[0].split(":");
                var namespace1 = namespaceObject[0];
                var object1 = namespaceObject[1];
                return checkRoleHierarchy(namespace1, object1, user, requiredRole);
            }
        }
        return related;
    }

    public List<String> getRelation(String namespace, String objectId, String user) {
        log.info("Getting relation for, namespace: {}, objectId: {}, and user: {}", namespace, objectId, user);
        Collection<String> relations = permissionRepository.findRelationsByNamespaceAndObjectIdAndUser(namespace, objectId, user);
        return List.copyOf(relations);
    }

    public List<Permission> getRelation(String namespace, String objectId, List<String> relationList) {
        log.info("Getting all relation for, namespace: {}, objectId: {}", namespace, objectId);

        List<Permission> relations = permissionRepository.findByNamespaceConfigAndObjectIdAndRelationIn(namespace, objectId, relationList);
        log.info("Possible permission map: {}", relations);
        return relations;
    }
}
