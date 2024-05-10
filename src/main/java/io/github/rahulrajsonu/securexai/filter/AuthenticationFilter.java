package io.github.rahulrajsonu.securexai.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.rahulrajsonu.securexai.config.TenantContext;
import io.github.rahulrajsonu.securexai.data.entity.Tenant;
import io.github.rahulrajsonu.securexai.data.model.AuthStrategy;
import io.github.rahulrajsonu.securexai.exception.ApiErrorResponse;
import io.github.rahulrajsonu.securexai.exception.AuthException;
import io.github.rahulrajsonu.securexai.security.AuthProvider;
import io.github.rahulrajsonu.securexai.security.provider.ApiKeyAuthProvider;
import io.github.rahulrajsonu.securexai.security.provider.JwtProvider;
import io.github.rahulrajsonu.securexai.security.provider.NoAuthProvider;
import io.github.rahulrajsonu.securexai.service.TenantService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Order(1)
@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final TenantService tenantService;
    private final BeanFactory beanFactory;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            var apiKey = request.getHeader("X-API-KEY");
            var apiSecret = request.getHeader("X-SECRET-KEY");
            var tenantId = request.getHeader("X-TenantID");
            TenantContext.setTenantId(tenantId);
            log.info("X-API-KEY: {}, X-SECRET-KEY: {}, X-TenantID: {}",apiKey,apiSecret,tenantId);
            Optional<Tenant> tenant = tenantService.findByTenantId(tenantId);
            if(tenant.isPresent()) {
                AuthStrategy authStrategy = tenant.get().getAuthStrategy();
                authenticate(authStrategy, request, response, filterChain);
            }
            filterChain.doFilter(request, response);
        } catch (AccessDeniedException e) {
            ApiErrorResponse errorResponse = new ApiErrorResponse(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write(toJson(errorResponse));
        } catch (AuthException ae){
            ApiErrorResponse errorResponse = new ApiErrorResponse(HttpServletResponse.SC_UNAUTHORIZED, ae.getMessage());
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write(toJson(errorResponse));
        }
    }

    private String toJson(ApiErrorResponse response) {
        try {
            return objectMapper.writeValueAsString(response);
        } catch (Exception e) {
            return "";
        }
    }

    private void authenticate(AuthStrategy strategy, HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        AuthProvider provider = switch (strategy){
            case Jwt -> beanFactory.getBean(JwtProvider.class);
            case ApiKey -> beanFactory.getBean(ApiKeyAuthProvider.class);
            case NoAuth -> beanFactory.getBean(NoAuthProvider.class);
        };
        provider.authorize(request,response,filterChain);
    }


}