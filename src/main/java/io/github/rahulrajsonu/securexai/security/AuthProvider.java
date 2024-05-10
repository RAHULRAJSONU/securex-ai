package io.github.rahulrajsonu.securexai.security;

import io.github.rahulrajsonu.securexai.data.security.User;
import io.github.rahulrajsonu.securexai.exception.AuthException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthProvider {
    void authorize(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws AuthException, ServletException, IOException;
}
