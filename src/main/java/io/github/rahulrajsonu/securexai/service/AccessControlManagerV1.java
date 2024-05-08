package io.github.rahulrajsonu.securexai.service;

import io.github.rahulrajsonu.securexai.data.entity.NamespaceConfig;
import io.github.rahulrajsonu.securexai.data.model.RelationTuple;
import lombok.Getter;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AccessControlManagerV1 {
    private final ExecutorService executorService;
    private final NamespaceConfigManager namespaceConfigManager;
    @Getter
    private final Map<String, NamespaceConfig> namespaceConfigs;
    private final int maxRecursionDepth;
    @Getter
    private final LRUCache<String, Boolean> cacheService;

    public AccessControlManagerV1(NamespaceConfigManager namespaceConfigManager, int maxRecursionDepth, int cacheCapacity) {
        this.namespaceConfigManager = namespaceConfigManager;
        this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        this.namespaceConfigs = new ConcurrentHashMap<>();
        this.maxRecursionDepth = maxRecursionDepth;
        this.cacheService = new LRUCache<>(cacheCapacity);
    }

    private static RelationTuple getRelationTuple(String user, RelationTuple tuple, String potentialUser) {
        String[] parts = potentialUser.split("#");
        RelationTuple newTuple;
        if (parts.length > 0 && parts[0].contains(":")) {
            String[] objRef = parts[0].split(":");
            String newNamespace = objRef[0];
            String newObjectId = objRef[1];
            newTuple = new RelationTuple(newNamespace, newObjectId, parts[1], user);
        } else {
            newTuple = new RelationTuple(tuple.namespace(), parts[0], parts[1], user);
        }
        return newTuple;
    }

    public CompletableFuture<Boolean> checkAccess(String user, RelationTuple tuple, int recursionDepth) {

        String cacheKey = user + ":" + tuple.toString();
        Boolean cachedResult = cacheService.get(cacheKey);
        if (cachedResult != null) {
            return CompletableFuture.completedFuture(cachedResult);
        }

        if (recursionDepth > maxRecursionDepth) {
            return CompletableFuture.failedFuture(new RuntimeException("Exceeded maximum recursion depth while checking for the access: " + cacheKey));
        }

        return CompletableFuture.supplyAsync(() -> {
            Set<String> users = namespaceConfigManager.getUsers(tuple.namespace(), tuple.objectId(), tuple.relation());
            if (users.contains(user)) {
                cacheService.put(cacheKey, true);
                return true;
            }
            for (String potentialUser : users) {
                if (potentialUser.contains("#")) {
                    RelationTuple newTuple = getRelationTuple(user, tuple, potentialUser);
                    try {
                        if (checkAccess(user, newTuple, recursionDepth + 1).get()) {
                            cacheService.put(cacheKey, true);
                            return true;
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        Thread.currentThread().interrupt();
                        System.err.println("Error during ACL check: " + e.getMessage());
                        return false;
                    }
                }
            }
            cacheService.put(cacheKey, false);
            return false;
        }, executorService);
    }

}
