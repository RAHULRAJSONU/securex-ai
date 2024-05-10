package io.github.rahulrajsonu.securexai.data.entity;

import io.github.rahulrajsonu.securexai.data.model.AuthStrategy;
import io.github.rahulrajsonu.securexai.data.model.Status;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@Setter
@Builder
@ToString
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Table(name="tenant")
@Accessors(chain = true)
public class Tenant {

    @Id
    @Column(unique = true, length = 256)
    private String tenantId;

    @Column(unique = true, length = 256, nullable = false)
    private String name;

    @Column(unique = true, length = 256)
    private String preferredTenantName;

    @Column(unique = true, length = 256)
    private String domain;

    private String primaryUser;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(unique = true, length = 256)
    private String accessKey;

    @Enumerated(EnumType.STRING)
    private AuthStrategy authStrategy;

    @Column(unique = true, length = 256)
    private String apiKey;

    private String apiSecret;
}