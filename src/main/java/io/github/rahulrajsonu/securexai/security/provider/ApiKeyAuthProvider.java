package io.github.rahulrajsonu.securexai.security.provider;

import io.github.rahulrajsonu.securexai.config.TenantContext;
import io.github.rahulrajsonu.securexai.data.entity.Tenant;
import io.github.rahulrajsonu.securexai.data.security.User;
import io.github.rahulrajsonu.securexai.exception.AuthException;
import io.github.rahulrajsonu.securexai.security.AuthProvider;
import io.github.rahulrajsonu.securexai.service.TenantService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class ApiKeyAuthProvider implements AuthProvider {

    private static final String AUTH_TOKEN_HEADER_NAME = "X-API-KEY";
    private static final String AUTH_SECRET_HEADER_NAME = "X-SECRET-KEY";

    private final TenantService tenantService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void authorize(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws AuthException, ServletException, IOException {
        String apiKey = request.getHeader(AUTH_TOKEN_HEADER_NAME);
        String apiSecret = request.getHeader(AUTH_SECRET_HEADER_NAME);
        Tenant tenant = tenantService.findByTenantId(TenantContext.getTenant())
                .orElseThrow(()->new AuthException("Invalid Tenant: "+TenantContext.getTenant()));
        if(!passwordEncoder.matches(apiSecret, tenant.getApiSecret())){
            SecurityContextHolder.clearContext();
            filterChain.doFilter(request, response);
        }
        UserDetails userDetails = User
                .builder()
                .userId(tenant.getTenantId())
                .username(tenant.getName())
                .tenantId(TenantContext.getTenant())
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_OWNER")))
                .build();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request,response);
    }
}
