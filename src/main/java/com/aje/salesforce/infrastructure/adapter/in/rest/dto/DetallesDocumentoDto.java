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
@Schema(description = "Detalles de Documento de Salesforce")
public class DetallesDocumentoDto {

    @Schema(description = "ID unico del detalle de documento", example = "a0X5f000000AbCdEFG")
    private String id;

    @Schema(description = "Indica si esta eliminado", example = "false")
    private Boolean isDeleted;

    @Schema(description = "Nombre del detalle de documento", example = "Detalle 001")
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

    @Schema(description = "ID de configuracion de impresion")
    private String configuracionDeImpresion;

    @Schema(description = "Documento de venta")
    private String documentoDeVenta;

    @Schema(description = "Texto 1")
    private String texto1;
}
