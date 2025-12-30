package com.aje.salesforce.infrastructure.adapter.out.persistence.repository;

import com.aje.salesforce.infrastructure.adapter.out.persistence.entity.CompaniaEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CompaniaRepository extends ReactiveCrudRepository<CompaniaEntity, String> {

    Flux<CompaniaEntity> findByPais(String pais);

    @Query("""
        INSERT INTO compania (id, owner_id, is_deleted, name, currency_iso_code, created_date, created_by_id,
            last_modified_date, last_modified_by_id, system_modstamp, last_activity_date, last_viewed_date,
            last_referenced_date, codigo__c, estado__c, pais__c, direccion__c, telefono__c, acronimo__c,
            integracion_erp__c, habilitar_seleccion_comprobante__c, habilitar_comprobante_preventa__c,
            habilitar_impuestos__c, impuesto__c, habilitar_extra_modulo__c, habilitar_linea_de_credito__c,
            acceso__c, correo_compania__c, enviar_a_oracle__c, enviar_a_siesa__c)
        VALUES (:#{#entity.id}, :#{#entity.ownerId}, :#{#entity.isDeleted}, :#{#entity.name}, :#{#entity.currencyIsoCode},
            :#{#entity.createdDate}, :#{#entity.createdById}, :#{#entity.lastModifiedDate}, :#{#entity.lastModifiedById},
            :#{#entity.systemModstamp}, :#{#entity.lastActivityDate}, :#{#entity.lastViewedDate},
            :#{#entity.lastReferencedDate}, :#{#entity.codigo}, :#{#entity.estado}, :#{#entity.pais},
            :#{#entity.direccion}, :#{#entity.telefono}, :#{#entity.acronimo}, :#{#entity.integracionErp},
            :#{#entity.habilitarSeleccionComprobante}, :#{#entity.habilitarComprobantePreventa},
            :#{#entity.habilitarImpuestos}, :#{#entity.impuesto}, :#{#entity.habilitarExtraModulo},
            :#{#entity.habilitarLineaDeCredito}, :#{#entity.acceso}, :#{#entity.correoCompania},
            :#{#entity.enviarAOracle}, :#{#entity.enviarASiesa})
        ON CONFLICT (id) DO UPDATE SET
            owner_id = EXCLUDED.owner_id, is_deleted = EXCLUDED.is_deleted, name = EXCLUDED.name,
            currency_iso_code = EXCLUDED.currency_iso_code, last_modified_date = EXCLUDED.last_modified_date,
            last_modified_by_id = EXCLUDED.last_modified_by_id, system_modstamp = EXCLUDED.system_modstamp,
            last_activity_date = EXCLUDED.last_activity_date, last_viewed_date = EXCLUDED.last_viewed_date,
            last_referenced_date = EXCLUDED.last_referenced_date, codigo__c = EXCLUDED.codigo__c,
            estado__c = EXCLUDED.estado__c, pais__c = EXCLUDED.pais__c, direccion__c = EXCLUDED.direccion__c,
            telefono__c = EXCLUDED.telefono__c, acronimo__c = EXCLUDED.acronimo__c,
            integracion_erp__c = EXCLUDED.integracion_erp__c,
            habilitar_seleccion_comprobante__c = EXCLUDED.habilitar_seleccion_comprobante__c,
            habilitar_comprobante_preventa__c = EXCLUDED.habilitar_comprobante_preventa__c,
            habilitar_impuestos__c = EXCLUDED.habilitar_impuestos__c, impuesto__c = EXCLUDED.impuesto__c,
            habilitar_extra_modulo__c = EXCLUDED.habilitar_extra_modulo__c,
            habilitar_linea_de_credito__c = EXCLUDED.habilitar_linea_de_credito__c,
            acceso__c = EXCLUDED.acceso__c, correo_compania__c = EXCLUDED.correo_compania__c,
            enviar_a_oracle__c = EXCLUDED.enviar_a_oracle__c, enviar_a_siesa__c = EXCLUDED.enviar_a_siesa__c
        """)
    Mono<Void> upsert(CompaniaEntity entity);
}
