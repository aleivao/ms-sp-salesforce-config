package com.aje.salesforce.infrastructure.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix = "salesforce")
public class SalesforceProperties {
    
    @NotBlank(message = "Salesforce login URL is required")
    private String loginUrl;
    
    @NotBlank(message = "Salesforce username is required")
    private String username;
    
    @NotBlank(message = "Salesforce password is required")
    private String password;
    
    @NotBlank(message = "Salesforce client ID is required")
    private String clientId;
    
    @NotBlank(message = "Salesforce client secret is required")
    private String clientSecret;
    
    @Positive(message = "Timeout must be positive")
    private Integer timeout = 30;
    
    @Positive(message = "Max connections must be positive")
    private Integer maxConnections = 100;
    
    @Positive(message = "Connection timeout must be positive")
    private Integer connectionTimeout = 10;
}
