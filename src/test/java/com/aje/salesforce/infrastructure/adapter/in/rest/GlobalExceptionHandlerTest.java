package com.aje.salesforce.infrastructure.adapter.in.rest;

import com.aje.salesforce.domain.exception.CompaniaNotFoundException;
import com.aje.salesforce.domain.exception.SalesforceIntegrationException;
import com.aje.salesforce.infrastructure.adapter.in.rest.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("GlobalExceptionHandler Tests")
class GlobalExceptionHandlerTest {
    
    private GlobalExceptionHandler exceptionHandler;
    private HttpServletRequest request;
    
    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
        request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/api/v1/companias/123");
    }
    
    @Test
    @DisplayName("Should handle CompaniaNotFoundException")
    void shouldHandleCompaniaNotFoundException() {
        // Given
        CompaniaNotFoundException exception = new CompaniaNotFoundException("123");
        
        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleCompaniaNotFoundException(exception, request);
        
        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(404);
        assertThat(response.getBody().getError()).isEqualTo("Not Found");
        assertThat(response.getBody().getMessage()).isEqualTo("Compañía con ID '123' no encontrada");
        assertThat(response.getBody().getPath()).isEqualTo("/api/v1/companias/123");
        assertThat(response.getBody().getTimestamp()).isNotNull();
    }
    
    @Test
    @DisplayName("Should handle SalesforceIntegrationException")
    void shouldHandleSalesforceIntegrationException() {
        // Given
        SalesforceIntegrationException exception = new SalesforceIntegrationException("Connection timeout");
        
        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleSalesforceIntegrationException(exception, request);
        
        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(503);
        assertThat(response.getBody().getError()).isEqualTo("Salesforce Integration Error");
        assertThat(response.getBody().getMessage()).contains("Error al comunicarse con Salesforce");
        assertThat(response.getBody().getPath()).isEqualTo("/api/v1/companias/123");
    }
    
    @Test
    @DisplayName("Should handle generic Exception")
    void shouldHandleGenericException() {
        // Given
        Exception exception = new RuntimeException("Unexpected error");

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleGeneralException(exception, request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(500);
        assertThat(response.getBody().getError()).isEqualTo("Internal Server Error");
        assertThat(response.getBody().getMessage()).isEqualTo("Ha ocurrido un error inesperado");
        assertThat(response.getBody().getPath()).isEqualTo("/api/v1/companias/123");
    }

    @Test
    @DisplayName("Should handle MethodArgumentNotValidException")
    void shouldHandleMethodArgumentNotValidException() {
        // Given
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("compania", "pais", "El país no puede estar vacío");

        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError));

        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(exception.getMessage()).thenReturn("Validation failed");

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleValidationException(exception, request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(400);
        assertThat(response.getBody().getError()).isEqualTo("Validation Error");
        assertThat(response.getBody().getMessage()).isEqualTo("Error en la validación de los parámetros");
        assertThat(response.getBody().getPath()).isEqualTo("/api/v1/companias/123");
        assertThat(response.getBody().getValidationErrors()).hasSize(1);
        assertThat(response.getBody().getValidationErrors().get(0).getField()).isEqualTo("pais");
        assertThat(response.getBody().getValidationErrors().get(0).getMessage()).isEqualTo("El país no puede estar vacío");
    }

    @Test
    @DisplayName("Should handle MethodArgumentTypeMismatchException")
    void shouldHandleMethodArgumentTypeMismatchException() {
        // Given
        MethodArgumentTypeMismatchException exception = new MethodArgumentTypeMismatchException(
            "value", String.class, "paramName", null, new RuntimeException("type error")
        );

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleTypeMismatchException(exception, request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(400);
        assertThat(response.getBody().getError()).isEqualTo("Invalid Parameter Type");
        assertThat(response.getBody().getMessage()).isEqualTo("El parámetro 'paramName' tiene un tipo inválido");
        assertThat(response.getBody().getPath()).isEqualTo("/api/v1/companias/123");
    }

    @Test
    @DisplayName("Should handle MethodArgumentNotValidException with multiple errors")
    void shouldHandleMethodArgumentNotValidExceptionWithMultipleErrors() {
        // Given
        BindingResult bindingResult = mock(BindingResult.class);

        FieldError fieldError1 = new FieldError("compania", "pais", "El país no puede estar vacío");
        FieldError fieldError2 = new FieldError("compania", "nombre", "El nombre es requerido");

        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError1, fieldError2));

        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(exception.getMessage()).thenReturn("Validation failed");

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleValidationException(exception, request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getValidationErrors()).hasSize(2);
    }
}
