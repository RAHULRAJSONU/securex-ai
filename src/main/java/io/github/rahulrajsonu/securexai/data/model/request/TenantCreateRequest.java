package io.github.rahulrajsonu.securexai.data.model.request;

import io.github.rahulrajsonu.securexai.data.model.AuthStrategy;
import io.github.rahulrajsonu.securexai.data.model.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TenantCreateRequest {

    @NotNull
    private String tenantId;

    @NotNull
    private String name;

    @NotNull
    private String preferredTenantName;

    @NotNull
    private String domain;

    @NotNull
    @Email
    private String primaryUser;

    @NotNull
    private Status status;

    @NotNull
    private String accessKey;

    @NotNull
    private AuthStrategy authStrategy;

}