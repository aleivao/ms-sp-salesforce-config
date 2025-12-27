package com.aje.salesforce.infrastructure.adapter.out.salesforce.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompaniaResponse {
    
    @JsonProperty("Id")
    private String id;
    
    @JsonProperty("Name")
    private String name;
    
    @JsonProperty("OwnerId")
    private String ownerId;
    
    @JsonProperty("IsDeleted")
    private Boolean isDeleted;
    
    @JsonProperty("CurrencyIsoCode")
    private String currencyIsoCode;
    
    @JsonProperty("CreatedDate")
    private ZonedDateTime createdDate;
    
    @JsonProperty("CreatedById")
    private String createdById;
    
    @JsonProperty("LastModifiedDate")
    private ZonedDateTime lastModifiedDate;
    
    @JsonProperty("LastModifiedById")
    private String lastModifiedById;
    
    @JsonProperty("SystemModstamp")
    private ZonedDateTime systemModstamp;
    
    @JsonProperty("LastActivityDate")
    private ZonedDateTime lastActivityDate;
    
    @JsonProperty("LastViewedDate")
    private ZonedDateTime lastViewedDate;
    
    @JsonProperty("LastReferencedDate")
    private ZonedDateTime lastReferencedDate;
    
    @JsonProperty("Codigo__c")
    private String codigo;
    
    @JsonProperty("Estado__c")
    private String estado;
    
    @JsonProperty("Pais__c")
    private String pais;
    
    @JsonProperty("Direccion__c")
    private String direccion;
    
    @JsonProperty("Telefono__c")
    private String telefono;
    
    @JsonProperty("Acronimo__c")
    private String acronimo;
    
    @JsonProperty("Integracion_ERP__c")
    private String integracionErp;
    
    @JsonProperty("Habilitar_seleccion_comprobante__c")
    private Boolean habilitarSeleccionComprobante;
    
    @JsonProperty("Habilitar_Comprobante_Preventa__c")
    private Boolean habilitarComprobantePreventa;
    
    @JsonProperty("Habilitar_Impuestos__c")
    private Boolean habilitarImpuestos;
    
    @JsonProperty("Impuesto__c")
    private String impuesto;
    
    @JsonProperty("Habilitar_extra_modulo__c")
    private Boolean habilitarExtraModulo;
    
    @JsonProperty("Habilitar_linea_de_credito__c")
    private Boolean habilitarLineaDeCredito;
    
    @JsonProperty("Acceso__c")
    private String acceso;
    
    @JsonProperty("Correo_compania__c")
    private String correoCompania;
    
    @JsonProperty("Enviar_a_Oracle__c")
    private Boolean enviarAOracle;
    
    @JsonProperty("Enviar_a_Siesa__c")
    private Boolean enviarASiesa;
    
    @JsonProperty("SELLIN__c")
    private Boolean sellin;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class QueryResult {
        
        @JsonProperty("totalSize")
        private Integer totalSize;
        
        @JsonProperty("done")
        private Boolean done;
        
        @JsonProperty("records")
        private List<CompaniaResponse> records;
    }
}
