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
@Table("tipo_de_documento")
public class TipoDocumentoEntity {

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

    @Column("activo__c")
    private Boolean activo;

    @Column("codigo__c")
    private String codigo;

    @Column("documento__c")
    private String documento;

    @Column("expresion_regular__c")
    private String expresionRegular;

    @Column("longitud__c")
    private Double longitud;

    @Column("pais__c")
    private String pais;

    @Column("tipo_de_persona__c")
    private String tipoDePersona;

    @Column("documento_venta__c")
    private String documentoVenta;

    @Column("documento_txt_eg__c")
    private String documentoTxtEg;
}
