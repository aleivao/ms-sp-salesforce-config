package com.aje.salesforce.infrastructure.adapter.out.persistence.repository;

import com.aje.salesforce.infrastructure.adapter.out.persistence.entity.TipoDocumentoEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface TipoDocumentoRepository extends ReactiveCrudRepository<TipoDocumentoEntity, String> {

    @Query("""
        INSERT INTO tipo_de_documento (id, owner_id, is_deleted, name, currency_iso_code, created_date,
            created_by_id, last_modified_date, last_modified_by_id, system_modstamp, last_activity_date,
            last_viewed_date, last_referenced_date, activo__c, codigo__c, documento__c, expresion_regular__c,
            longitud__c, pais__c, tipo_de_persona__c, documento_venta__c, documento_txt_eg__c)
        VALUES (:#{#entity.id}, :#{#entity.ownerId}, :#{#entity.isDeleted}, :#{#entity.name}, :#{#entity.currencyIsoCode},
            :#{#entity.createdDate}, :#{#entity.createdById}, :#{#entity.lastModifiedDate}, :#{#entity.lastModifiedById},
            :#{#entity.systemModstamp}, :#{#entity.lastActivityDate}, :#{#entity.lastViewedDate},
            :#{#entity.lastReferencedDate}, :#{#entity.activo}, :#{#entity.codigo}, :#{#entity.documento},
            :#{#entity.expresionRegular}, :#{#entity.longitud}, :#{#entity.pais}, :#{#entity.tipoDePersona},
            :#{#entity.documentoVenta}, :#{#entity.documentoTxtEg})
        ON CONFLICT (id) DO UPDATE SET
            owner_id = EXCLUDED.owner_id, is_deleted = EXCLUDED.is_deleted, name = EXCLUDED.name,
            currency_iso_code = EXCLUDED.currency_iso_code, last_modified_date = EXCLUDED.last_modified_date,
            last_modified_by_id = EXCLUDED.last_modified_by_id, system_modstamp = EXCLUDED.system_modstamp,
            last_activity_date = EXCLUDED.last_activity_date, last_viewed_date = EXCLUDED.last_viewed_date,
            last_referenced_date = EXCLUDED.last_referenced_date, activo__c = EXCLUDED.activo__c,
            codigo__c = EXCLUDED.codigo__c, documento__c = EXCLUDED.documento__c,
            expresion_regular__c = EXCLUDED.expresion_regular__c, longitud__c = EXCLUDED.longitud__c,
            pais__c = EXCLUDED.pais__c, tipo_de_persona__c = EXCLUDED.tipo_de_persona__c,
            documento_venta__c = EXCLUDED.documento_venta__c, documento_txt_eg__c = EXCLUDED.documento_txt_eg__c
        """)
    Mono<Void> upsert(TipoDocumentoEntity entity);
}
