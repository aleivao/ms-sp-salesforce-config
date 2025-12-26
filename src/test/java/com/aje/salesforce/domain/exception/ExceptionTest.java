package com.aje.salesforce.domain.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Exception Tests")
class ExceptionTest {
    
    @Test
    @DisplayName("Should create CompaniaNotFoundException with ID")
    void shouldCreateCompaniaNotFoundExceptionWithId() {
        CompaniaNotFoundException exception = new CompaniaNotFoundException("123");
        
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("Compañía con ID '123' no encontrada");
    }
    
    @Test
    @DisplayName("Should create CompaniaNotFoundException with message and cause")
    void shouldCreateCompaniaNotFoundExceptionWithMessageAndCause() {
        Throwable cause = new RuntimeException("Root cause");
        CompaniaNotFoundException exception = new CompaniaNotFoundException("Custom message", cause);
        
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("Custom message");
        assertThat(exception.getCause()).isEqualTo(cause);
    }
    
    @Test
    @DisplayName("Should create SalesforceIntegrationException with message")
    void shouldCreateSalesforceIntegrationExceptionWithMessage() {
        SalesforceIntegrationException exception = new SalesforceIntegrationException("Connection failed");
        
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("Connection failed");
    }
    
    @Test
    @DisplayName("Should create SalesforceIntegrationException with message and cause")
    void shouldCreateSalesforceIntegrationExceptionWithMessageAndCause() {
        Throwable cause = new RuntimeException("Network error");
        SalesforceIntegrationException exception = new SalesforceIntegrationException("Failed to connect", cause);
        
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("Failed to connect");
        assertThat(exception.getCause()).isEqualTo(cause);
    }
}
