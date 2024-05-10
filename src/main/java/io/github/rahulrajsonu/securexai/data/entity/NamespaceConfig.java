package io.github.rahulrajsonu.securexai.data.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Set;

@Entity
@Getter
@Setter
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class NamespaceConfig extends AbstractPersistable{

    @Column(nullable = false)
    private String namespace;

    @OneToMany(mappedBy = "namespaceConfig", cascade = CascadeType.ALL)
    private Set<Permission> permissions;

    private boolean roleHierarchyEnabled;

}