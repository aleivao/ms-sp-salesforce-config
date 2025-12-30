package com.aje.salesforce.infrastructure.adapter.out.salesforce.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DetallesDocumentoResponse {

    @JsonProperty("Id")
    private String id;

    @JsonProperty("IsDeleted")
    private Boolean isDeleted;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("CurrencyIsoCode")
    private String currencyIsoCode;

    @JsonProperty("CreatedDate")
    private String createdDate;

    @JsonProperty("CreatedById")
    private String createdById;

    @JsonProperty("LastModifiedDate")
    private String lastModifiedDate;

    @JsonProperty("LastModifiedById")
    private String lastModifiedById;

    @JsonProperty("SystemModstamp")
    private String systemModstamp;

    @JsonProperty("Configuracion_de_impresion__c")
    private String configuracionDeImpresion;

    @JsonProperty("Documento_de_venta__c")
    private String documentoDeVenta;

    @JsonProperty("Texto_1__c")
    private String texto1;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class QueryResult {
        private int totalSize;
        private boolean done;
        private java.util.List<DetallesDocumentoResponse> records;
    }
}
