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
@Table("detalles_documento")
public class DetallesDocumentoEntity {

    @Id
    @Column("id")
    private String id;

    @Column("is_deleted")
    private Boolean isDeleted;

    @Column("name")
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

    @Column("configuracion_de_impresion__c")
    private String configuracionDeImpresion;

    @Column("documento_de_venta__c")
    private String documentoDeVenta;

    @Column("texto_1__c")
    private String texto1;
}
