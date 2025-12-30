package com.aje.salesforce.infrastructure.adapter.out.persistence.repository;

import com.aje.salesforce.infrastructure.adapter.out.persistence.entity.ConfiguracionImpresionEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ConfiguracionImpresionRepository extends ReactiveCrudRepository<ConfiguracionImpresionEntity, String> {

    @Query("""
        INSERT INTO configuracion_de_impresion (id, owner_id, is_deleted, name, currency_iso_code, created_date,
            created_by_id, last_modified_date, last_modified_by_id, system_modstamp, last_viewed_date,
            last_referenced_date, compania__c, imp1__c, imp2__c, imp3__c, imp4__c, numero_documento__c,
            pais__c, texto_1__c, texto_2__c, texto_3__c, tipo_documento__c, sucursal__c, documento_default__c)
        VALUES (:#{#entity.id}, :#{#entity.ownerId}, :#{#entity.isDeleted}, :#{#entity.name}, :#{#entity.currencyIsoCode},
            :#{#entity.createdDate}, :#{#entity.createdById}, :#{#entity.lastModifiedDate}, :#{#entity.lastModifiedById},
            :#{#entity.systemModstamp}, :#{#entity.lastViewedDate}, :#{#entity.lastReferencedDate},
            :#{#entity.compania}, :#{#entity.imp1}, :#{#entity.imp2}, :#{#entity.imp3}, :#{#entity.imp4},
            :#{#entity.numeroDocumento}, :#{#entity.pais}, :#{#entity.texto1}, :#{#entity.texto2}, :#{#entity.texto3},
            :#{#entity.tipoDocumento}, :#{#entity.sucursal}, :#{#entity.documentoDefault})
        ON CONFLICT (id) DO UPDATE SET
            owner_id = EXCLUDED.owner_id, is_deleted = EXCLUDED.is_deleted, name = EXCLUDED.name,
            currency_iso_code = EXCLUDED.currency_iso_code, last_modified_date = EXCLUDED.last_modified_date,
            last_modified_by_id = EXCLUDED.last_modified_by_id, system_modstamp = EXCLUDED.system_modstamp,
            last_viewed_date = EXCLUDED.last_viewed_date, last_referenced_date = EXCLUDED.last_referenced_date,
            compania__c = EXCLUDED.compania__c, imp1__c = EXCLUDED.imp1__c, imp2__c = EXCLUDED.imp2__c,
            imp3__c = EXCLUDED.imp3__c, imp4__c = EXCLUDED.imp4__c, numero_documento__c = EXCLUDED.numero_documento__c,
            pais__c = EXCLUDED.pais__c, texto_1__c = EXCLUDED.texto_1__c, texto_2__c = EXCLUDED.texto_2__c,
            texto_3__c = EXCLUDED.texto_3__c, tipo_documento__c = EXCLUDED.tipo_documento__c,
            sucursal__c = EXCLUDED.sucursal__c, documento_default__c = EXCLUDED.documento_default__c
        """)
    Mono<Void> upsert(ConfiguracionImpresionEntity entity);
}
