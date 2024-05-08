package io.github.rahulrajsonu.securexai.service;

import io.github.rahulrajsonu.securexai.data.entity.NamespaceConfig;

import java.util.Set;

public interface NamespaceConfigManager {
    void addPermission(String namespace, String objectId, String relation, Set<String> users);

    Set<String> getUsers(String namespace, String objectId, String relation);

    NamespaceConfig getNamespaceConfig(String namespace);
}