package io.github.rahulrajsonu.securexai.config;

import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CurrentTenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver<String>, HibernatePropertiesCustomizer {

    @Value("${spring.application.name}")
    private String DEFAULT_TENANT_ID;

    @Override
    public String resolveCurrentTenantIdentifier() {
        String tenantId = TenantContext.getTenant();
        if (!ObjectUtils.isEmpty(tenantId)) {
            return tenantId;
        } else {
            return DEFAULT_TENANT_ID;
        }
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }

    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put(AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, this);
    }

}