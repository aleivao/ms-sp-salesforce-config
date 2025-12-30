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
@Schema(description = "Configuracion de Impresion de Salesforce")
public class ConfiguracionImpresionDto {

    @Schema(description = "ID unico de la configuracion", example = "a0X5f000000AbCdEFG")
    private String id;

    @Schema(description = "ID del propietario")
    private String ownerId;

    @Schema(description = "Indica si esta eliminado", example = "false")
    private Boolean isDeleted;

    @Schema(description = "Nombre de la configuracion", example = "Config Factura PE")
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

    @Schema(description = "Fecha de ultima visualizacion")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastViewedDate;

    @Schema(description = "Fecha de ultima referencia")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastReferencedDate;

    @Schema(description = "ID de la compania relacionada")
    private String compania;

    @Schema(description = "Impresora 1")
    private String imp1;

    @Schema(description = "Impresora 2")
    private String imp2;

    @Schema(description = "Impresora 3")
    private String imp3;

    @Schema(description = "Impresora 4")
    private String imp4;

    @Schema(description = "Numero de documento")
    private String numeroDocumento;

    @Schema(description = "Pais de la configuracion", example = "PE")
    private String pais;

    @Schema(description = "Texto 1")
    private String texto1;

    @Schema(description = "Texto 2")
    private String texto2;

    @Schema(description = "Texto 3")
    private String texto3;

    @Schema(description = "Tipo de documento", example = "Factura")
    private String tipoDocumento;

    @Schema(description = "ID de la sucursal relacionada")
    private String sucursal;

    @Schema(description = "Documento por defecto", example = "true")
    private Boolean documentoDefault;
}
