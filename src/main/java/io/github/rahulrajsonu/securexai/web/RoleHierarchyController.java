package io.github.rahulrajsonu.securexai.web;

import io.github.rahulrajsonu.securexai.data.entity.RoleHierarchy;
import io.github.rahulrajsonu.securexai.service.RoleHierarchyService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("securex/v1/role")
public class RoleHierarchyController {
    private final RoleHierarchyService service;

    @GetMapping("/hierarchy/{namespace}/{objectId}")
    public ResponseEntity<?> getRoleHierarchy(@NonNull @PathVariable String namespace,
                                              @NonNull @PathVariable String objectId) {
        return ResponseEntity.ok(service.getHierarchy(namespace, objectId));
    }

    @GetMapping("/hierarchy/activate/{namespace}")
    public ResponseEntity<?> activateRoleHierarchy(@NonNull @PathVariable String namespace) {
        return ResponseEntity.ok(service.activateRoleHierarchy(namespace) ? "Role Hierarchy Activated for Namespace: " + namespace : "");
    }

    @PostMapping("/hierarchy")
    public ResponseEntity<?> save(@RequestBody RoleHierarchy roleHierarchy) {
        return ResponseEntity.ok(service.save(roleHierarchy));
    }

}
