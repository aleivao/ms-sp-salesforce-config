package com.aje.salesforce.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    
    @Value("${spring.application.name}")
    private String applicationName;
    
    @Value("${spring.application.version:1.0.0}")
    private String applicationVersion;
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Salesforce Configuration Service API")
                .description("Microservicio para gestión de configuración de compañías desde Salesforce")
                .version(applicationVersion)
                .contact(new Contact()
                    .name("AJE Group - DevOps Team")
                    .email("devops@ajegroup.com")
                    .url("https://www.ajegroup.com")
                )
                .license(new License()
                    .name("Proprietary")
                    .url("https://www.ajegroup.com/license")
                )
            )
            .servers(List.of(
                new Server()
                    .url("https://salesforce-config.ajegroup.com")
                    .description("Production server"),
                new Server()
                    .url("https://qa-salesforce-config.ajegroup.com")
                    .description("QA server"),
                new Server()
                    .url("https://dev-salesforce-config.ajegroup.com")
                    .description("Development server"),
                new Server()
                    .url("http://localhost:8080")
                    .description("Local server")
            ));
    }
}
