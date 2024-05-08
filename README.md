# securex-ai
Zanzibar - Spring boot implementation 
This is an implementation of Google Zanzibar modal blend with role hierarchy evaluation.

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