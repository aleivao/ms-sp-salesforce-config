package com.aje.salesforce.infrastructure.adapter.out.persistence.repository;

import com.aje.salesforce.infrastructure.adapter.out.persistence.entity.DetallesDocumentoEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface DetallesDocumentoRepository extends ReactiveCrudRepository<DetallesDocumentoEntity, String> {

    @Query("INSERT INTO detalles_documento (id, is_deleted, name, currency_iso_code, created_date, created_by_id, " +
           "last_modified_date, last_modified_by_id, system_modstamp, configuracion_de_impresion__c, " +
           "documento_de_venta__c, texto_1__c) " +
           "VALUES (:#{#entity.id}, :#{#entity.isDeleted}, :#{#entity.name}, :#{#entity.currencyIsoCode}, " +
           ":#{#entity.createdDate}, :#{#entity.createdById}, :#{#entity.lastModifiedDate}, :#{#entity.lastModifiedById}, " +
           ":#{#entity.systemModstamp}, :#{#entity.configuracionDeImpresion}, :#{#entity.documentoDeVenta}, " +
           ":#{#entity.texto1}) " +
           "ON CONFLICT (id) DO UPDATE SET " +
           "is_deleted = EXCLUDED.is_deleted, name = EXCLUDED.name, currency_iso_code = EXCLUDED.currency_iso_code, " +
           "created_date = EXCLUDED.created_date, created_by_id = EXCLUDED.created_by_id, " +
           "last_modified_date = EXCLUDED.last_modified_date, last_modified_by_id = EXCLUDED.last_modified_by_id, " +
           "system_modstamp = EXCLUDED.system_modstamp, configuracion_de_impresion__c = EXCLUDED.configuracion_de_impresion__c, " +
           "documento_de_venta__c = EXCLUDED.documento_de_venta__c, texto_1__c = EXCLUDED.texto_1__c " +
           "RETURNING *")
    reactor.core.publisher.Mono<DetallesDocumentoEntity> upsert(DetallesDocumentoEntity entity);

    Flux<DetallesDocumentoEntity> findByConfiguracionDeImpresion(String configuracionDeImpresion);
}
