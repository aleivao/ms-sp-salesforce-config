package com.aje.salesforce.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetallesDocumento {
    private String id;
    private Boolean isDeleted;
    private String name;
    private String currencyIsoCode;
    private LocalDateTime createdDate;
    private String createdById;
    private LocalDateTime lastModifiedDate;
    private String lastModifiedById;
    private LocalDateTime systemModstamp;
    private String configuracionDeImpresion;
    private String documentoDeVenta;
    private String texto1;
}
