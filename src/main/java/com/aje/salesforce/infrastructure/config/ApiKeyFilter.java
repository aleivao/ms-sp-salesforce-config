package com.aje.salesforce.infrastructure.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiKeyFilter extends OncePerRequestFilter {

    private final ApiKeyProperties apiKeyProperties;

    private static final Set<String> PUBLIC_PATHS = Set.of(
            "/actuator",
            "/swagger-ui",
            "/v3/api-docs",
            "/swagger-resources"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String requestPath = request.getRequestURI();

        if (isPublicPath(requestPath)) {
            filterChain.doFilter(request, response);
            return;
        }

        String apiKey = request.getHeader(apiKeyProperties.getHeaderName());

        if (apiKey == null || apiKey.isBlank()) {
            log.warn("API Key missing for request: {} {}", request.getMethod(), requestPath);
            sendUnauthorizedResponse(response, "API Key is required");
            return;
        }

        if (!apiKey.equals(apiKeyProperties.getValue())) {
            log.warn("Invalid API Key for request: {} {}", request.getMethod(), requestPath);
            sendUnauthorizedResponse(response, "Invalid API Key");
            return;
        }

        log.debug("API Key validated for request: {} {}", request.getMethod(), requestPath);
        filterChain.doFilter(request, response);
    }

    private boolean isPublicPath(String path) {
        return PUBLIC_PATHS.stream().anyMatch(path::startsWith);
    }

    private void sendUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(String.format(
                "{\"status\":401,\"error\":\"Unauthorized\",\"message\":\"%s\"}", message
        ));
    }
}
