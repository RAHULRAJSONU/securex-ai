package io.github.rahulrajsonu.securexai.security.provider;

import io.github.rahulrajsonu.securexai.config.TenantContext;
import io.github.rahulrajsonu.securexai.data.security.User;
import io.github.rahulrajsonu.securexai.exception.AuthException;
import io.github.rahulrajsonu.securexai.security.AuthProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class NoAuthProvider implements AuthProvider {
    @Override
    public void authorize(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws AuthException, ServletException, IOException {
        log.info("Auth strategy: NoAuth, allowing this flow.");
        UserDetails userDetails = User
                .builder()
                .username(TenantContext.getTenant())
                .tenantId(TenantContext.getTenant())
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_VIEWER")))
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
