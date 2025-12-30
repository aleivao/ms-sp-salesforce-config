package com.aje.salesforce.infrastructure.adapter.out.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("sucursal")
public class SucursalEntity {

    @Id
    private String id;

    @Column("is_deleted")
    private Boolean isDeleted;

    private String name;

    @Column("currency_iso_code")
    private String currencyIsoCode;

    @Column("created_date")
    private LocalDateTime createdDate;

    @Column("created_by_id")
    private String createdById;

    @Column("last_modified_date")
    private LocalDateTime lastModifiedDate;

    @Column("last_modified_by_id")
    private String lastModifiedById;

    @Column("system_modstamp")
    private LocalDateTime systemModstamp;

    @Column("last_activity_date")
    private LocalDate lastActivityDate;

    @Column("last_viewed_date")
    private LocalDateTime lastViewedDate;

    @Column("last_referenced_date")
    private LocalDateTime lastReferencedDate;

    @Column("compania__c")
    private String compania;

    @Column("codigo__c")
    private String codigo;

    @Column("codigo_de_compania__c")
    private String codigoDeCompania;

    @Column("codigo_unico__c")
    private String codigoUnico;

    @Column("pais__c")
    private String pais;

    @Column("almacenista__c")
    private String almacenista;

    @Column("eje_territorial__c")
    private String ejeTerritorial;

    @Column("habilitar_extra_modulo__c")
    private Boolean habilitarExtraModulo;

    @Column("habilitar_linea_de_credito__c")
    private Boolean habilitarLineaDeCredito;

    @Column("habilitar_transferencia_gratuita__c")
    private Boolean habilitarTransferenciaGratuita;

    @Column("tipo_de_sucursal__c")
    private String tipoDeSucursal;

    @Column("enviar_a_oracle__c")
    private Boolean enviarAOracle;

    @Column("almacen__c")
    private String almacen;

    @Column("bloques_de_envio_de_pedido__c")
    private String bloquesDeEnvioDePedido;

    @Column("enviar_por_bloques__c")
    private Boolean enviarPorBloques;

    @Column("bloquear_envio_erp__c")
    private Boolean bloquearEnvioErp;

    @Column("limite_reenvio_pedidos__c")
    private Double limiteReenvioPedidos;

    @Column("minutos_reeenvio_pedido_en_espera__c")
    private Double minutosReenvioPedidoEnEspera;

    @Column("reenvio_pedidos_en_espera__c")
    private Boolean reenvioPedidosEnEspera;

    @Column("minutos_reenvio_pedido_default__c")
    private Double minutosReenvioPedidoDefault;

    @Column("facturacion_punto_de_venta__c")
    private Boolean facturacionPuntoDeVenta;

    @Column("estado__c")
    private String estado;

    @Column("enviar_a_siesa__c")
    private Boolean enviarASiesa;
}
