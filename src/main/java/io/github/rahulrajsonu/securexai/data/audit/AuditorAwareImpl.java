package io.github.rahulrajsonu.securexai.data.audit;

import io.github.rahulrajsonu.securexai.data.security.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        try {
            User user = (User) authentication.getPrincipal();

            return Optional.of(user.getUserId());

        } catch (ClassCastException e) {

            try {
                org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

                if (user.getUsername().equals("spring")) {
                    // set value as userId, required in tests
                    return Optional.of("TEST_USER");
                }

                return Optional.of("TEST_USER");

            } catch (ClassCastException e1) {
                // anonymousUser, in case of scheduled jobs
                return Optional.of("anonymousUser");
            }

        }
    }
}