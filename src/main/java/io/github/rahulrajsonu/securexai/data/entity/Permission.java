package io.github.rahulrajsonu.securexai.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Set;

@Entity
@Getter
@Setter
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"objectId", "relation", "namespaceConfig_id"})})
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "namespace_config_id", nullable = false)
    private NamespaceConfig namespaceConfig;

    private String objectId;

    private String relation;

    @ElementCollection
    @CollectionTable(name = "permission_users", joinColumns = @JoinColumn(name = "permission_id"))
    @Column(name = "user")
    private Set<String> users;
}