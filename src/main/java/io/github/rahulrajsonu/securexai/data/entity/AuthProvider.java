package io.github.rahulrajsonu.securexai.data.entity;

import io.github.rahulrajsonu.securexai.data.model.HttpMethod;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;

import java.util.HashMap;
import java.util.Map;

@Entity
@Getter
@Setter
@Builder
@ToString
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Table(name="auth_provider")
public class AuthProvider extends AbstractPersistable {
    private String name;
    private String type;
    private String identitySource="$request.header.Authorization";
    private String tokenType;
    private HttpMethod requestMethod;
    private String issuerURL;
    private String audience;
    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    private Map<String, String> headers = new HashMap<>();
    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    private Map<String, String> params = new HashMap<>();
}