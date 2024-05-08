package io.github.rahulrajsonu.securexai.service;

import io.github.rahulrajsonu.securexai.data.model.ParsedExpression;
import io.github.rahulrajsonu.securexai.data.model.RelationTuple;
import io.github.rahulrajsonu.securexai.parser.ExpressionParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class AccessControlManager {
    private static final int MAX_RECURSION_DEPTH = 10;
    private final NamespaceConfigService namespaceConfigService;

    @Autowired
    public AccessControlManager(NamespaceConfigService namespaceConfigService) {
        this.namespaceConfigService = namespaceConfigService;
    }

//    @Cacheable(value = "accessControlCache", key = "#user + '-' + #tuple.toString()")
    public boolean checkAccess(String user, RelationTuple tuple, int recursionDepth) {
        if (recursionDepth > MAX_RECURSION_DEPTH) {
            throw new RuntimeException("Exceeded maximum recursion depth while checking for the access: " + user + "-" + tuple.toString());
        }
        Set<String> users = namespaceConfigService.getUsers(tuple.namespace(), tuple.objectId(), tuple.relation());
        if (users.contains(user)) {
            return true;
        }

        Set<RelationTuple> visitedTuples = new HashSet<>();
        visitedTuples.add(tuple);

        for (String potentialUser : users) {
            if (potentialUser.contains("#")) {
                RelationTuple newTuple = getRelationTuple(user, tuple, potentialUser);
                if (!visitedTuples.contains(newTuple) && checkAccess(user, newTuple, recursionDepth + 1)) {
                    return true;
                }
            }
        }
        return namespaceConfigService.checkRoleHierarchy(tuple.namespace(), tuple.objectId(), tuple.user(), tuple.relation());
    }

    public void handleExpression(List<String> expressions) {
        for (String expression : expressions) {
            ParsedExpression parsedExpression = ExpressionParser.parseExpression(expression);
            log.info("Parsing expression: {} => Semantics: {}", expression, parsedExpression.semantic());
            namespaceConfigService.addPermission(parsedExpression.namespace(), parsedExpression.objectId(),
                    parsedExpression.relation(), parsedExpression.users());
        }
    }

    @CacheEvict(value = "accessControlCache", allEntries = true)
    public void clearCache() {
        // Clear cache if needed
    }

    private RelationTuple getRelationTuple(String user, RelationTuple tuple, String potentialUser) {
        String[] parts = potentialUser.split("#");
        if (parts.length > 0 && parts[0].contains(":")) {
            String[] objRef = parts[0].split(":");
            String newNamespace = objRef[0];
            String newObjectId = objRef[1];
            return new RelationTuple(newNamespace, newObjectId, parts[1], user);
        } else {
            return new RelationTuple(tuple.namespace(), parts[0], parts[1], user);
        }
    }
}

