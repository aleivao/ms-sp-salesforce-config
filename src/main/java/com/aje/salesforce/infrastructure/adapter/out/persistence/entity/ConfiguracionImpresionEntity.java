package com.aje.salesforce.infrastructure.adapter.out.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("configuracion_de_impresion")
public class ConfiguracionImpresionEntity {

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

    @Column("last_viewed_date")
    private LocalDateTime lastViewedDate;

    @Column("last_referenced_date")
    private LocalDateTime lastReferencedDate;

    @Column("compania__c")
    private String compania;

    @Column("imp1__c")
    private String imp1;

    @Column("imp2__c")
    private String imp2;

    @Column("imp3__c")
    private String imp3;

    @Column("imp4__c")
    private String imp4;

    @Column("numero_documento__c")
    private String numeroDocumento;

    @Column("pais__c")
    private String pais;

    @Column("texto_1__c")
    private String texto1;

    @Column("texto_2__c")
    private String texto2;

    @Column("texto_3__c")
    private String texto3;

    @Column("tipo_documento__c")
    private String tipoDocumento;

    @Column("sucursal__c")
    private String sucursal;

    @Column("documento_default__c")
    private Boolean documentoDefault;
}
