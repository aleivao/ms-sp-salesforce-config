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
public class ConfiguracionImpresion {
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
    private LocalDateTime lastViewedDate;
    private LocalDateTime lastReferencedDate;
    private String compania;
    private String imp1;
    private String imp2;
    private String imp3;
    private String imp4;
    private String numeroDocumento;
    private String pais;
    private String texto1;
    private String texto2;
    private String texto3;
    private String tipoDocumento;
    private String sucursal;
    private Boolean documentoDefault;
}
