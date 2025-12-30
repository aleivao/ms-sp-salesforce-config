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
public class SucursalResponse {

    @JsonProperty("Id")
    private String id;

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

    @JsonProperty("Compania__c")
    private String compania;

    @JsonProperty("Codigo__c")
    private String codigo;

    @JsonProperty("Codigo_de_Compania__c")
    private String codigoDeCompania;

    @JsonProperty("Codigo_Unico__c")
    private String codigoUnico;

    @JsonProperty("Pais__c")
    private String pais;

    @JsonProperty("Almacenista__c")
    private String almacenista;

    @JsonProperty("Eje_Territorial__c")
    private String ejeTerritorial;

    @JsonProperty("Habilitar_Extra_Modulo__c")
    private Boolean habilitarExtraModulo;

    @JsonProperty("Habilitar_Linea_de_Credito__c")
    private Boolean habilitarLineaDeCredito;

    @JsonProperty("Habilitar_Transferencia_Gratuita__c")
    private Boolean habilitarTransferenciaGratuita;

    @JsonProperty("Tipo_de_Sucursal__c")
    private String tipoDeSucursal;

    @JsonProperty("Enviar_a_Oracle__c")
    private Boolean enviarAOracle;

    @JsonProperty("Almacen__c")
    private String almacen;

    @JsonProperty("Bloques_de_Envio_de_Pedido__c")
    private String bloquesDeEnvioDePedido;

    @JsonProperty("Enviar_por_Bloques__c")
    private Boolean enviarPorBloques;

    @JsonProperty("Bloquear_Envio_ERP__c")
    private Boolean bloquearEnvioErp;

    @JsonProperty("Limite_Reenvio_Pedidos__c")
    private Double limiteReenvioPedidos;

    @JsonProperty("Minutos_Reeenvio_Pedido_en_Espera__c")
    private Double minutosReenvioPedidoEnEspera;

    @JsonProperty("Reenvio_Pedidos_en_Espera__c")
    private Boolean reenvioPedidosEnEspera;

    @JsonProperty("Minutos_Reenvio_Pedido_Default__c")
    private Double minutosReenvioPedidoDefault;

    @JsonProperty("Facturacion_Punto_de_Venta__c")
    private Boolean facturacionPuntoDeVenta;

    @JsonProperty("Estado__c")
    private String estado;

    @JsonProperty("Enviar_a_Siesa__c")
    private Boolean enviarASiesa;

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
        private List<SucursalResponse> records;
    }
}
