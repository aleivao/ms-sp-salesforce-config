package com.aje.salesforce.domain.model;

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
public class TipoDocumento {
    private String id;
    private String ownerId;
    private Boolean isDeleted;
    private String name;
    private String currencyIsoCode;
    private LocalDateTime createdDate;
    private String createdById;
    private LocalDateTime lastModifiedDate;
    private String lastModifiedById;
    private LocalDateTime systemModstamp;
    private LocalDate lastActivityDate;
    private LocalDateTime lastViewedDate;
    private LocalDateTime lastReferencedDate;
    private Boolean activo;
    private String codigo;
    private String documento;
    private String expresionRegular;
    private Double longitud;
    private String pais;
    private String tipoDePersona;
    private String documentoVenta;
    private String documentoTxtEg;
}
