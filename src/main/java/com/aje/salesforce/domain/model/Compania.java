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
public class Compania {
    
    private String id;
    private String name;
    private String ownerId;
    private Boolean isDeleted;
    private String currencyIsoCode;
    
    private LocalDateTime createdDate;
    private String createdById;
    private LocalDateTime lastModifiedDate;
    private String lastModifiedById;
    
    private LocalDateTime systemModstamp;
    private LocalDateTime lastActivityDate;
    private LocalDateTime lastViewedDate;
    private LocalDateTime lastReferencedDate;
    
    private String codigo;
    private String estado;
    private String pais;
    private String direccion;
    private String telefono;
    private String acronimo;
    
    private String integracionErp;
    private Boolean habilitarSeleccionComprobante;
    private Boolean habilitarComprobantePreventa;
    private Boolean habilitarImpuestos;
    private String impuesto;
    
    private Boolean habilitarExtraModulo;
    private Boolean habilitarLineaDeCredito;
    
    private String acceso;
    private String correoCompania;
    private Boolean enviarAOracle;
    private Boolean enviarASiesa;
    private Boolean sellin;
    
    public boolean isActive() {
        return Boolean.FALSE.equals(isDeleted) && "Activo".equalsIgnoreCase(estado);
    }
    
    public boolean belongsToCountry(String country) {
        return country != null && country.equalsIgnoreCase(this.pais);
    }
}
