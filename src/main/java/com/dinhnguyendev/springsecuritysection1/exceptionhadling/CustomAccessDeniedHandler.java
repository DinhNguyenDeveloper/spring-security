package com.dinhnguyendev.springsecuritysection1.exceptionhadling;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {


        LocalDateTime currentDateTime = LocalDateTime.now();
        String message = Objects.nonNull(accessDeniedException) && Objects.nonNull(accessDeniedException.getMessage())
                ? accessDeniedException.getMessage()
                : "Unauthorized";
        String path = request.getRequestURI();
        response.setHeader("custom-denied-reason", "Authorization Failed");
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json;charset=UTF-8");
        // Json response
        String jsonFormat =
                String.format(
                        "{\"timestamp\": \"%s\", \"status\": %d, \"error\": \"%s\", \"message\": \"%s\", \"path\": \"%s\"}",
                        currentDateTime, HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                        message, path);
        response.getWriter().write(jsonFormat);
    }
}
