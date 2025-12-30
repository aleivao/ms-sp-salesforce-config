package com.aje.salesforce.infrastructure.adapter.in.rest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Tipo de Documento de Salesforce")
public class TipoDocumentoDto {

    @Schema(description = "ID unico del tipo de documento", example = "a0X5f000000AbCdEFG")
    private String id;

    @Schema(description = "ID del propietario")
    private String ownerId;

    @Schema(description = "Indica si esta eliminado", example = "false")
    private Boolean isDeleted;

    @Schema(description = "Nombre del tipo de documento", example = "DNI")
    private String name;

    @Schema(description = "Codigo ISO de moneda", example = "PEN")
    private String currencyIsoCode;

    @Schema(description = "Fecha de creacion")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdDate;

    @Schema(description = "ID del creador")
    private String createdById;

    @Schema(description = "Fecha de ultima modificacion")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastModifiedDate;

    @Schema(description = "ID del ultimo modificador")
    private String lastModifiedById;

    @Schema(description = "Timestamp de ultima modificacion del sistema")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime systemModstamp;

    @Schema(description = "Fecha de ultima actividad")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate lastActivityDate;

    @Schema(description = "Fecha de ultima visualizacion")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastViewedDate;

    @Schema(description = "Fecha de ultima referencia")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastReferencedDate;

    @Schema(description = "Estado activo", example = "true")
    private Boolean activo;

    @Schema(description = "Codigo del documento", example = "DNI")
    private String codigo;

    @Schema(description = "Documento")
    private String documento;

    @Schema(description = "Expresion regular para validacion")
    private String expresionRegular;

    @Schema(description = "Longitud del documento")
    private Double longitud;

    @Schema(description = "Pais del tipo de documento", example = "PE")
    private String pais;

    @Schema(description = "Tipo de persona")
    private String tipoDePersona;

    @Schema(description = "Documento de venta")
    private String documentoVenta;

    @Schema(description = "Documento TXT EG")
    private String documentoTxtEg;
}
