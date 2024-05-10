package io.github.rahulrajsonu.securexai.filter;

import io.github.rahulrajsonu.securexai.config.TenantContext;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(1)
class TenantFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        String tenantName = req.getHeader("X-TenantID");
        TenantContext.setTenantId(tenantName);
        try {
            chain.doFilter(request, response);
        } finally {
            TenantContext.setTenantId("ANONYMOUS");
        }
    }
}