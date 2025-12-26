package com.aje.salesforce.infrastructure.adapter.in.rest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta de error estándar")
public class ErrorResponse {
    
    @Schema(description = "Timestamp del error")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;
    
    @Schema(description = "Código de estado HTTP", example = "404")
    private Integer status;
    
    @Schema(description = "Mensaje de error", example = "Resource not found")
    private String error;
    
    @Schema(description = "Mensaje detallado", example = "Compañía con ID 'abc123' no encontrada")
    private String message;
    
    @Schema(description = "Path de la petición", example = "/api/v1/companias/abc123")
    private String path;
    
    @Schema(description = "Errores de validación")
    private List<ValidationError> validationErrors;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Error de validación")
    public static class ValidationError {
        
        @Schema(description = "Campo con error", example = "pais")
        private String field;
        
        @Schema(description = "Mensaje de error", example = "El país no puede estar vacío")
        private String message;
    }
}
