package io.github.rahulrajsonu.securexai.web;

import io.github.rahulrajsonu.securexai.data.entity.NamespaceConfig;
import io.github.rahulrajsonu.securexai.data.model.RelationTuple;
import io.github.rahulrajsonu.securexai.service.AccessControlManager;
import io.github.rahulrajsonu.securexai.service.NamespaceConfigService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/namespace")
public class ZanzibarController {
    private final NamespaceConfigService namespaceConfigService;
    private final AccessControlManager accessControlManager;

    @Autowired
    public ZanzibarController(NamespaceConfigService namespaceConfigService, AccessControlManager accessControlManager) {
        this.namespaceConfigService = namespaceConfigService;
        this.accessControlManager = accessControlManager;
    }

    @PostMapping("/update")
    public void handlePermissionExpression(@RequestBody List<String> expression) {
        accessControlManager.handleExpression(expression);
        log.info("Permission added: {}", expression);
    }

    @GetMapping("/users/{namespace}/object/{objectId}/relation/{relation}")
    public Set<String> getUsersWithAccess(String namespace, String objectId, String relation) {
        return namespaceConfigService.getUsers(namespace, objectId, relation);
    }

    @GetMapping
    public NamespaceConfig getNamespaceConfig(String namespace) {
        return namespaceConfigService.getNamespaceConfig(namespace);
    }

    @GetMapping("/check")
    public Boolean hasAccess(@RequestParam String user, @RequestParam String namespace, @RequestParam String objectId, @RequestParam String relation) {
        RelationTuple tuple = new RelationTuple(namespace, objectId, relation, user);
        return accessControlManager.checkAccess(user, tuple, 0);
    }

    @PostMapping("/bulk/grant/{namespace}/{relation}")
    public void grantAccessToNamespace(@NonNull String namespace, @NonNull String relation, Set<String> users) {
        namespaceConfigService.addPermission(namespace, "*", relation, users);
        log.info("Access granted to users for all objects in the {} namespace.", namespace);
    }

    @GetMapping("/users")
    public Set<String> getUsersWithAccessInNamespace(@RequestParam String namespace, @RequestParam String relation) {
        return namespaceConfigService.getUsers(namespace, "*", relation);
    }
}