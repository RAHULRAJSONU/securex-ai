package io.github.rahulrajsonu.securexai.web;

import io.github.rahulrajsonu.securexai.data.entity.Tenant;
import io.github.rahulrajsonu.securexai.data.model.request.TenantCreateRequest;
import io.github.rahulrajsonu.securexai.service.TenantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("securex/v1/tenant")
public class TenantController {

    private final TenantService tenantService;

    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody TenantCreateRequest createRequest){
        log.info("Registering new tenant: {}",createRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(tenantService.register(modelMapper.map(createRequest, Tenant.class)));
    }
}
