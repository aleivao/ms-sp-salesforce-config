package com.aje.salesforce.infrastructure.adapter.in.rest;

import com.aje.salesforce.domain.exception.CompaniaNotFoundException;
import com.aje.salesforce.domain.exception.ConfiguracionImpresionNotFoundException;
import com.aje.salesforce.domain.exception.DetallesDocumentoNotFoundException;
import com.aje.salesforce.domain.exception.SalesforceIntegrationException;
import com.aje.salesforce.domain.exception.SucursalNotFoundException;
import com.aje.salesforce.domain.exception.TipoDocumentoNotFoundException;
import com.aje.salesforce.infrastructure.adapter.in.rest.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(CompaniaNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCompaniaNotFoundException(
        CompaniaNotFoundException ex,
        HttpServletRequest request
    ) {
        log.error("Compañía no encontrada: {}", ex.getMessage());
        
        ErrorResponse error = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.NOT_FOUND.value())
            .error(HttpStatus.NOT_FOUND.getReasonPhrase())
            .message(ex.getMessage())
            .path(request.getRequestURI())
            .build();
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(SucursalNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleSucursalNotFoundException(
        SucursalNotFoundException ex,
        HttpServletRequest request
    ) {
        log.error("Sucursal no encontrada: {}", ex.getMessage());

        ErrorResponse error = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.NOT_FOUND.value())
            .error(HttpStatus.NOT_FOUND.getReasonPhrase())
            .message(ex.getMessage())
            .path(request.getRequestURI())
            .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(ConfiguracionImpresionNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleConfiguracionImpresionNotFoundException(
        ConfiguracionImpresionNotFoundException ex,
        HttpServletRequest request
    ) {
        log.error("Configuracion de impresion no encontrada: {}", ex.getMessage());

        ErrorResponse error = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.NOT_FOUND.value())
            .error(HttpStatus.NOT_FOUND.getReasonPhrase())
            .message(ex.getMessage())
            .path(request.getRequestURI())
            .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(TipoDocumentoNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTipoDocumentoNotFoundException(
        TipoDocumentoNotFoundException ex,
        HttpServletRequest request
    ) {
        log.error("Tipo de documento no encontrado: {}", ex.getMessage());

        ErrorResponse error = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.NOT_FOUND.value())
            .error(HttpStatus.NOT_FOUND.getReasonPhrase())
            .message(ex.getMessage())
            .path(request.getRequestURI())
            .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(DetallesDocumentoNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleDetallesDocumentoNotFoundException(
        DetallesDocumentoNotFoundException ex,
        HttpServletRequest request
    ) {
        log.error("Detalles de documento no encontrado: {}", ex.getMessage());

        ErrorResponse error = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.NOT_FOUND.value())
            .error(HttpStatus.NOT_FOUND.getReasonPhrase())
            .message(ex.getMessage())
            .path(request.getRequestURI())
            .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(SalesforceIntegrationException.class)
    public ResponseEntity<ErrorResponse> handleSalesforceIntegrationException(
        SalesforceIntegrationException ex,
        HttpServletRequest request
    ) {
        log.error("Error de integración con Salesforce: {}", ex.getMessage(), ex);
        
        ErrorResponse error = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.SERVICE_UNAVAILABLE.value())
            .error("Salesforce Integration Error")
            .message("Error al comunicarse con Salesforce: " + ex.getMessage())
            .path(request.getRequestURI())
            .build();
        
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
        MethodArgumentNotValidException ex,
        HttpServletRequest request
    ) {
        log.error("Error de validación: {}", ex.getMessage());
        
        List<ErrorResponse.ValidationError> validationErrors = ex.getBindingResult()
            .getAllErrors()
            .stream()
            .map(error -> ErrorResponse.ValidationError.builder()
                .field(((FieldError) error).getField())
                .message(error.getDefaultMessage())
                .build())
            .collect(Collectors.toList());
        
        ErrorResponse error = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .error("Validation Error")
            .message("Error en la validación de los parámetros")
            .path(request.getRequestURI())
            .validationErrors(validationErrors)
            .build();
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatchException(
        MethodArgumentTypeMismatchException ex,
        HttpServletRequest request
    ) {
        log.error("Error de tipo de parámetro: {}", ex.getMessage());
        
        ErrorResponse error = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .error("Invalid Parameter Type")
            .message(String.format("El parámetro '%s' tiene un tipo inválido", ex.getName()))
            .path(request.getRequestURI())
            .build();
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(
        Exception ex,
        HttpServletRequest request
    ) {
        log.error("Error inesperado: {}", ex.getMessage(), ex);
        
        ErrorResponse error = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
            .message("Ha ocurrido un error inesperado")
            .path(request.getRequestURI())
            .build();
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
