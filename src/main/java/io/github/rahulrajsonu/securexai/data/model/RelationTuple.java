package io.github.rahulrajsonu.securexai.data.model;

public record RelationTuple(String namespace, String objectId, String relation, String user) {
    @Override
    public String toString() {
        return String.format("%s:%s#%s@%s", namespace, objectId, relation, user);
    }
}