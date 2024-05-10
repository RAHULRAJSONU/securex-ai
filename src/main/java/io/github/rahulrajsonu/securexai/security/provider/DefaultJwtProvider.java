package io.github.rahulrajsonu.securexai.security.provider;

import io.github.rahulrajsonu.securexai.config.TenantContext;
import io.github.rahulrajsonu.securexai.data.entity.AuthProvider;
import io.github.rahulrajsonu.securexai.data.entity.Client;
import io.github.rahulrajsonu.securexai.data.security.User;
import io.github.rahulrajsonu.securexai.exception.AuthException;
import io.github.rahulrajsonu.securexai.security.JwtService;
import io.github.rahulrajsonu.securexai.security.authorizer.ClientService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@AllArgsConstructor
public class DefaultJwtProvider implements JwtProvider {

    private static final Logger log = LoggerFactory.getLogger(DefaultJwtProvider.class);
    private final ClientService clientService;
    private final JwtService jwtService;

    @Override
    public boolean verifyToken(String token) {
        return true;
    }

    @Override
    public void authorize(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws AuthException, ServletException, IOException {
        Client client = clientService.getClient(TenantContext.getTenant())
                .orElseThrow(()->new AuthException("No auth client found"));
        AuthProvider authProvider = client.getAuthProvider();
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        Jwt jwt = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            jwt = jwtService.parse(token, authProvider.getIssuerURL());
            log.info("Parsed jwt: {}",jwt);
            username = jwt.getSubject();
        }
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = User
                    .builder()
                    .userId(username)
                    .username(username)
                    .tenantId(jwt.getClaimAsString("tenantId"))
                    .authorities(jwt.getClaim("roles"))
                    .build();
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(request, response);
    }
}
