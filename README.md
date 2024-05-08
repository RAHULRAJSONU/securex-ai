# securex-ai
Zanzibar - Spring boot implementation \
This is an implementation of Google Zanzibar modal blend with role hierarchy evaluation.

## Summary
```markdown
Google Zanzibar is an authorization system that provides a flexible and scalable model for managing access control in distributed systems. 
It is designed to handle complex authorization requirements across various services and resources while maintaining security and scalability. 
The Zanzibar model is based on a set of high-level principles:

1. Attributes-Based Access Control (ABAC): Zanzibar utilizes ABAC, where access decisions are based on attributes associated with users, 
   resources, and the environment.
2. Hierarchical Namespaces: It organizes resources into hierarchical namespaces, enabling efficient and flexible access control policies.
3. Global Namespace Management: Zanzibar supports global management of namespaces, allowing for centralized policy management and enforcement.
4. Decentralized Policy Evaluation: Authorization policies can be defined and evaluated independently for each namespace, providing autonomy 
   and flexibility to service owners.
5. Scalable Policy Evaluation: The system is designed to handle a large number of policy evaluations efficiently, 
   ensuring scalability even in complex distributed environments.
   
Overall, Google Zanzibar offers a comprehensive and scalable solution for managing access control in distributed systems, supporting diverse 
authorization requirements across a wide range of applications and services.
```

## Configure database properties in the application.yml file
```yaml
spring:
  application.name: securex-ai
  datasource:
    url: jdbc:mysql://localhost:3306/securex-ai?useSSL=false
    username: root
    password: root
    driverClassName: com.mysql.cj.jdbc.Driver
```
## Start the spring boot application and access the swagger ui at
http://localhost:8080/swagger-ui/index.html

## Add the sample expressions
http://localhost:8080/swagger-ui/index.html#/zanzibar-controller/handlePermissionExpression
```json
[
    "group:eng#member@user:10",
    "group:eng#member@user:11",
    "group:eng#member@user:12",
    "group:eng#member@user:13",
    "doc:readme#owner@user:10",
    "folder:A#owner@user:10",
    "doc:readme#parent@folder:A",
    "doc:readme#viewer@group:eng#member",
    "group:eng-senior#member@user:10",
    "folder:A#viewer@group:eng#member",
    "repo:secureX/secureXAi#owner@organization:secureX",
    "repo:secureX/secureXAi#admin@team:secureX/core#member",
    "repo:secureX/secureXAi#reader@user:11",
    "team:secureX/core#member@team:secureX/backend#member",
    "team:secureX/backend#member@user:10"
]
```

## To enable role hierarchy add hierarchy for namespace and objectId
http://localhost:8080/swagger-ui/index.html#/role-hierarchy-controller/save
```json
{
  "name": "team role hierarchy",
  "expression": "owner > admin > editor > member > viewer > reader",
  "namespace": "team",
  "objectId": "secureX/core",
  "active": true
}
```
## Activate role hierarchy evaluation 
http://localhost:8080/swagger-ui/index.html#/role-hierarchy-controller/activateRoleHierarchy/{namespace}

## Check for the relation of subject to the object
http://localhost:8080/namespace/check?user=user:10&namespace=team&objectId=secureX/core&relation=viewer

## Reference
https://play.fga.dev/sandbox/?store=github \
https://zanzibar.academy/ \
https://research.google/pubs/zanzibar-googles-consistent-global-authorization-system/