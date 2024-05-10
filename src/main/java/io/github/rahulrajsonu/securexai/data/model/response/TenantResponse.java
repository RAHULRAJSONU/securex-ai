package io.github.rahulrajsonu.securexai.data.model.response;

import io.github.rahulrajsonu.securexai.data.model.AuthStrategy;
import io.github.rahulrajsonu.securexai.data.model.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TenantResponse {

    private String tenantId;

    private String name;

    private String preferredTenantName;

    private String domain;

    private String primaryUser;

    private Status status;

    private String accessKey;

    private AuthStrategy authStrategy;

    private String apiKey;

    private String apiSecret;
}