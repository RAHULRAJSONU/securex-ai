package io.github.rahulrajsonu.securexai.data.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SignedUserHelper {

    /**
     * get user id of signed user from spring security context
     * @return user id of signed user
     */
    public static User user() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            return ((User) auth.getPrincipal());
        } catch (ClassCastException e) {
            var testUser = new User();
            testUser.setUserId("TEST-USER");
            return testUser;
        }
    }

    public static String userId() {
        return user().getUserId();
    }
}