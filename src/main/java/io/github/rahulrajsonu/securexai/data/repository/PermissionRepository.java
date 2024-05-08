package io.github.rahulrajsonu.securexai.data.repository;

import io.github.rahulrajsonu.securexai.data.entity.NamespaceConfig;
import io.github.rahulrajsonu.securexai.data.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Set<Permission> findByNamespaceConfig(NamespaceConfig namespaceConfig);

    boolean existsByObjectId(String objectId);

    Set<Permission> findByNamespaceConfigAndObjectIdAndRelation(NamespaceConfig namespaceConfig, String objectId, String relation);

    Set<Permission> findByNamespaceConfigAndRelation(NamespaceConfig namespaceConfig, String relation);

    @Query("""
                 SELECT DISTINCT p.relation FROM Permission p 
                     WHERE 
                         p.namespaceConfig.namespace = :namespace AND 
                         p.objectId = :objectId AND 
                         :user MEMBER OF p.users
            """)
    Collection<String> findRelationsByNamespaceAndObjectIdAndUser(@Param("namespace") String namespace,
                                                                  @Param("objectId") String objectId,
                                                                  @Param("user") String user);
    @Query("""
            SELECT p FROM Permission p
            WHERE p.namespaceConfig.namespace = :namespace AND p.objectId = :objectId AND p.relation IN :relations
            """)
    List<Permission> findByNamespaceConfigAndObjectIdAndRelationIn(@Param("namespace") String namespace,
                                                                   @Param("objectId") String objectId,
                                                                   @Param("relations") List<String> relations);

    Permission findByObjectIdAndRelationAndNamespaceConfigNamespace(String objectId, String relation, String namespace);
}