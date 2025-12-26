package com.aje.salesforce.infrastructure.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "security.api-key")
public class ApiKeyProperties {

    private String headerName = "X-API-Key";
    private String value;
}
