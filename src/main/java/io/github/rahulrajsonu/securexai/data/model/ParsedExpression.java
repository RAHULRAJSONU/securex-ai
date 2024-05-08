package io.github.rahulrajsonu.securexai.data.model;

import java.util.Set;

/**
 * @param namespace Getters for parsed expression
 */
public record ParsedExpression(
        String namespace,
        String objectId,
        String relation,
        Set<String> users,
        String expression,
        String semantic
) {
}