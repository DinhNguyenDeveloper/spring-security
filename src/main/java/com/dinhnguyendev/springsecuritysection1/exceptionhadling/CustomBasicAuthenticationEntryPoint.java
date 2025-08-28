package com.dinhnguyendev.springsecuritysection1.exceptionhadling;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomBasicAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        LocalDateTime currentDateTime = LocalDateTime.now();
        String message = Objects.nonNull(authException) && Objects.nonNull(authException.getMessage())
                ? authException.getMessage()
                : "Unauthorized";
        String path = request.getRequestURI();
        response.setHeader("custom-error-reason", "Authentication Failed");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");
        // Json response
        String jsonFormat =
                String.format(
                        "{\"timestamp\": \"%s\", \"status\": %d, \"error\": \"%s\", \"message\": \"%s\", \"path\": \"%s\"}",
                        currentDateTime, HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                        message, path);
        response.getWriter().write(jsonFormat);
    }
}
