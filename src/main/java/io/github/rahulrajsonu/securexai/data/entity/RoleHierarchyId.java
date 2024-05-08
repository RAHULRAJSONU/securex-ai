package io.github.rahulrajsonu.securexai.data.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class RoleHierarchyId implements Serializable {
    @Serial
    private static final long serialVersionUID = -729426009901390792L;
    private String namespace;
    private String objectId;
}
