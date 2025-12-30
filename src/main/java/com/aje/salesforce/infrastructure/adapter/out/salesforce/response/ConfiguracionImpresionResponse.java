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
public class ConfiguracionImpresionResponse {

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

    @JsonProperty("LastViewedDate")
    private ZonedDateTime lastViewedDate;

    @JsonProperty("LastReferencedDate")
    private ZonedDateTime lastReferencedDate;

    @JsonProperty("Compania__c")
    private String compania;

    @JsonProperty("Imp1__c")
    private String imp1;

    @JsonProperty("Imp2__c")
    private String imp2;

    @JsonProperty("Imp3__c")
    private String imp3;

    @JsonProperty("Imp4__c")
    private String imp4;

    @JsonProperty("Numero_Documento__c")
    private String numeroDocumento;

    @JsonProperty("Pais__c")
    private String pais;

    @JsonProperty("Texto_1__c")
    private String texto1;

    @JsonProperty("Texto_2__c")
    private String texto2;

    @JsonProperty("Texto_3__c")
    private String texto3;

    @JsonProperty("Tipo_Documento__c")
    private String tipoDocumento;

    @JsonProperty("Sucursal__c")
    private String sucursal;

    @JsonProperty("Documento_Default__c")
    private Boolean documentoDefault;

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
        private List<ConfiguracionImpresionResponse> records;
    }
}
