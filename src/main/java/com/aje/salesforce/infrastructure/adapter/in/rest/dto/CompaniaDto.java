package com.aje.salesforce.infrastructure.adapter.in.rest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Compañía de Salesforce")
public class CompaniaDto {
    
    @Schema(description = "ID único de la compañía", example = "a0X5f000000AbCdEFG")
    private String id;
    
    @Schema(description = "Nombre de la compañía", example = "AJE PERU")
    private String name;
    
    @Schema(description = "ID del propietario", example = "0055f000000AbCdEFG")
    private String ownerId;
    
    @Schema(description = "Indica si está eliminado", example = "false")
    private Boolean isDeleted;
    
    @Schema(description = "Código ISO de moneda", example = "PEN")
    private String currencyIsoCode;
    
    @Schema(description = "Fecha de creación")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdDate;
    
    @Schema(description = "ID del creador")
    private String createdById;
    
    @Schema(description = "Fecha de última modificación")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastModifiedDate;
    
    @Schema(description = "ID del último modificador")
    private String lastModifiedById;
    
    @Schema(description = "Timestamp de última modificación del sistema")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime systemModstamp;
    
    @Schema(description = "Fecha de última actividad")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastActivityDate;
    
    @Schema(description = "Fecha de última visualización")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastViewedDate;
    
    @Schema(description = "Fecha de última referencia")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastReferencedDate;
    
    @Schema(description = "Código de la compañía", example = "PE001")
    private String codigo;
    
    @Schema(description = "Estado de la compañía", example = "Activo")
    private String estado;
    
    @Schema(description = "País de la compañía", example = "Peru")
    private String pais;
    
    @Schema(description = "Dirección de la compañía")
    private String direccion;
    
    @Schema(description = "Teléfono de la compañía")
    private String telefono;
    
    @Schema(description = "Acrónimo de la compañía", example = "AJE")
    private String acronimo;
    
    @Schema(description = "Integración ERP")
    private String integracionErp;
    
    @Schema(description = "Habilitar selección de comprobante")
    private Boolean habilitarSeleccionComprobante;
    
    @Schema(description = "Habilitar comprobante de preventa")
    private Boolean habilitarComprobantePreventa;
    
    @Schema(description = "Habilitar impuestos")
    private Boolean habilitarImpuestos;
    
    @Schema(description = "Tipo de impuesto")
    private String impuesto;
    
    @Schema(description = "Habilitar módulo extra")
    private Boolean habilitarExtraModulo;
    
    @Schema(description = "Habilitar línea de crédito")
    private Boolean habilitarLineaDeCredito;
    
    @Schema(description = "Acceso")
    private String acceso;
    
    @Schema(description = "Correo de la compañía")
    private String correoCompania;
    
    @Schema(description = "Enviar a Oracle")
    private Boolean enviarAOracle;
    
    @Schema(description = "Enviar a Siesa")
    private Boolean enviarASiesa;
    
    @Schema(description = "SELLIN")
    private Boolean sellin;
}
