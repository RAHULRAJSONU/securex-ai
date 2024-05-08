package io.github.rahulrajsonu.securexai.parser;

import io.github.rahulrajsonu.securexai.data.model.ParsedExpression;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Utility class for parsing permission expressions and generating semantics.
 */
public class ExpressionParser {
    /**
     * @param expression doc:readme#viewer@user&group:eng#member&time:business-hours
     * @return ParsedExpression
     */
    public static ParsedExpression parseExpression(String expression) {
        Objects.requireNonNull(expression, "Expression must not be null");
        System.out.println("Parsing expression: " + expression);
        int atIndex = Attribute.RELATION_USER_SEPARATOR.indexIn(expression);
        if (atIndex == -1) {
            throw new IllegalArgumentException("Expression format error, '@' required to separate object and subject: " + expression);
        }

        String objectPart = expression.substring(0, atIndex);
        String subjectPart = expression.substring(atIndex + 1);

        String[] objectSplit = Attribute.OBJECT_RELATION_SEPARATOR.split(objectPart);
        if (objectSplit.length != 2) {
            throw new IllegalArgumentException("Invalid object format: " + objectPart);
        }

        String[] namespaceObject = Attribute.NAMESPACE_OBJECT_SEPARATOR.split(objectSplit[0]);
        if (namespaceObject.length != 2) {
            throw new IllegalArgumentException("Invalid namespace or object ID format: " + objectSplit[0]);
        }

        String namespace = namespaceObject[0];
        String objectId = namespaceObject[1];
        String relation = objectSplit[1];

        Set<String> users = parseUsers(subjectPart);
        var semantics = generateSemanticsV1(namespace, objectId, relation, users, Attribute.NAMESPACE_OBJECT_SEPARATOR.split(subjectPart)[0]);
        return new ParsedExpression(namespace, objectId, relation, users, expression, semantics);
    }

    private static Set<String> parseUsers(String userExpression) {
        Set<String> users = new HashSet<>();
        if (Attribute.COMBINER.presentIn(userExpression)) {
            users.addAll(Attribute.COMBINER.splitToList(userExpression));
        } else {
            users.add(userExpression);
        }
        return users;
    }

    private static String generateSemanticsV1(String namespace, String objectId, String relation, Set<String> users, String subjectType) {
        var obj = namespace + ":" + objectId;
        return String.format("%s %s %s %s of %s %s", subjectType, users, users.size() > 1 ? "are the" : "is an", relation, namespace, objectId);
    }
}