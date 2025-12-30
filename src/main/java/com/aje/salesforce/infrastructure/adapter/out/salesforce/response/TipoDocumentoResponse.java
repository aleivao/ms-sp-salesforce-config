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
public class TipoDocumentoResponse {

    @JsonProperty("Id")
    private String id;

    @JsonProperty("OwnerId")
    private String ownerId;

    @JsonProperty("IsDeleted")
    private Boolean isDeleted;

    @JsonProperty("Name")
    private String name;

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
    private String lastActivityDate;

    @JsonProperty("LastViewedDate")
    private ZonedDateTime lastViewedDate;

    @JsonProperty("LastReferencedDate")
    private ZonedDateTime lastReferencedDate;

    @JsonProperty("Activo__c")
    private Boolean activo;

    @JsonProperty("Codigo__c")
    private String codigo;

    @JsonProperty("Documento__c")
    private String documento;

    @JsonProperty("Expresion_Regular__c")
    private String expresionRegular;

    @JsonProperty("Longitud__c")
    private Double longitud;

    @JsonProperty("Pais__c")
    private String pais;

    @JsonProperty("Tipo_de_Persona__c")
    private String tipoDePersona;

    @JsonProperty("Documento_Venta__c")
    private String documentoVenta;

    @JsonProperty("Documento_TXT_EG__c")
    private String documentoTxtEg;

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
        private List<TipoDocumentoResponse> records;
    }
}
