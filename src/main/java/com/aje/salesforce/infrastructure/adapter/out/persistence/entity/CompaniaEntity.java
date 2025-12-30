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
@Table("compania")
public class CompaniaEntity {

    @Id
    private String id;

    @Column("owner_id")
    private String ownerId;

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

    @Column("codigo__c")
    private String codigo;

    @Column("estado__c")
    private String estado;

    @Column("pais__c")
    private String pais;

    @Column("direccion__c")
    private String direccion;

    @Column("telefono__c")
    private String telefono;

    @Column("acronimo__c")
    private String acronimo;

    @Column("integracion_erp__c")
    private Boolean integracionErp;

    @Column("habilitar_seleccion_comprobante__c")
    private Boolean habilitarSeleccionComprobante;

    @Column("habilitar_comprobante_preventa__c")
    private Boolean habilitarComprobantePreventa;

    @Column("habilitar_impuestos__c")
    private Boolean habilitarImpuestos;

    @Column("impuesto__c")
    private Double impuesto;

    @Column("habilitar_extra_modulo__c")
    private Boolean habilitarExtraModulo;

    @Column("habilitar_linea_de_credito__c")
    private Boolean habilitarLineaDeCredito;

    @Column("acceso__c")
    private String acceso;

    @Column("correo_compania__c")
    private String correoCompania;

    @Column("enviar_a_oracle__c")
    private Boolean enviarAOracle;

    @Column("enviar_a_siesa__c")
    private Boolean enviarASiesa;

    @Column("sellin__c")
    private Boolean sellin;
}
