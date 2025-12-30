package com.aje.salesforce.infrastructure.adapter.out.persistence.repository;

import com.aje.salesforce.infrastructure.adapter.out.persistence.entity.SucursalEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface SucursalRepository extends ReactiveCrudRepository<SucursalEntity, String> {

    @Query("""
        INSERT INTO sucursal (id, is_deleted, name, currency_iso_code, created_date, created_by_id,
            last_modified_date, last_modified_by_id, system_modstamp, last_activity_date, last_viewed_date,
            last_referenced_date, compania__c, codigo__c, codigo_de_compania__c, codigo_unico__c, pais__c,
            almacenista__c, eje_territorial__c, habilitar_extra_modulo__c, habilitar_linea_de_credito__c,
            habilitar_transferencia_gratuita__c, tipo_de_sucursal__c, enviar_a_oracle__c, almacen__c,
            bloques_de_envio_de_pedido__c, enviar_por_bloques__c, bloquear_envio_erp__c,
            limite_reenvio_pedidos__c, minutos_reeenvio_pedido_en_espera__c, reenvio_pedidos_en_espera__c,
            minutos_reenvio_pedido_default__c, facturacion_punto_de_venta__c, estado__c, enviar_a_siesa__c)
        VALUES (:#{#entity.id}, :#{#entity.isDeleted}, :#{#entity.name}, :#{#entity.currencyIsoCode},
            :#{#entity.createdDate}, :#{#entity.createdById}, :#{#entity.lastModifiedDate}, :#{#entity.lastModifiedById},
            :#{#entity.systemModstamp}, :#{#entity.lastActivityDate}, :#{#entity.lastViewedDate},
            :#{#entity.lastReferencedDate}, :#{#entity.compania}, :#{#entity.codigo}, :#{#entity.codigoDeCompania},
            :#{#entity.codigoUnico}, :#{#entity.pais}, :#{#entity.almacenista}, :#{#entity.ejeTerritorial},
            :#{#entity.habilitarExtraModulo}, :#{#entity.habilitarLineaDeCredito}, :#{#entity.habilitarTransferenciaGratuita},
            :#{#entity.tipoDeSucursal}, :#{#entity.enviarAOracle}, :#{#entity.almacen},
            :#{#entity.bloquesDeEnvioDePedido}, :#{#entity.enviarPorBloques}, :#{#entity.bloquearEnvioErp},
            :#{#entity.limiteReenvioPedidos}, :#{#entity.minutosReenvioPedidoEnEspera}, :#{#entity.reenvioPedidosEnEspera},
            :#{#entity.minutosReenvioPedidoDefault}, :#{#entity.facturacionPuntoDeVenta}, :#{#entity.estado},
            :#{#entity.enviarASiesa})
        ON CONFLICT (id) DO UPDATE SET
            is_deleted = EXCLUDED.is_deleted, name = EXCLUDED.name, currency_iso_code = EXCLUDED.currency_iso_code,
            last_modified_date = EXCLUDED.last_modified_date, last_modified_by_id = EXCLUDED.last_modified_by_id,
            system_modstamp = EXCLUDED.system_modstamp, last_activity_date = EXCLUDED.last_activity_date,
            last_viewed_date = EXCLUDED.last_viewed_date, last_referenced_date = EXCLUDED.last_referenced_date,
            compania__c = EXCLUDED.compania__c, codigo__c = EXCLUDED.codigo__c,
            codigo_de_compania__c = EXCLUDED.codigo_de_compania__c, codigo_unico__c = EXCLUDED.codigo_unico__c,
            pais__c = EXCLUDED.pais__c, almacenista__c = EXCLUDED.almacenista__c,
            eje_territorial__c = EXCLUDED.eje_territorial__c, habilitar_extra_modulo__c = EXCLUDED.habilitar_extra_modulo__c,
            habilitar_linea_de_credito__c = EXCLUDED.habilitar_linea_de_credito__c,
            habilitar_transferencia_gratuita__c = EXCLUDED.habilitar_transferencia_gratuita__c,
            tipo_de_sucursal__c = EXCLUDED.tipo_de_sucursal__c, enviar_a_oracle__c = EXCLUDED.enviar_a_oracle__c,
            almacen__c = EXCLUDED.almacen__c, bloques_de_envio_de_pedido__c = EXCLUDED.bloques_de_envio_de_pedido__c,
            enviar_por_bloques__c = EXCLUDED.enviar_por_bloques__c, bloquear_envio_erp__c = EXCLUDED.bloquear_envio_erp__c,
            limite_reenvio_pedidos__c = EXCLUDED.limite_reenvio_pedidos__c,
            minutos_reeenvio_pedido_en_espera__c = EXCLUDED.minutos_reeenvio_pedido_en_espera__c,
            reenvio_pedidos_en_espera__c = EXCLUDED.reenvio_pedidos_en_espera__c,
            minutos_reenvio_pedido_default__c = EXCLUDED.minutos_reenvio_pedido_default__c,
            facturacion_punto_de_venta__c = EXCLUDED.facturacion_punto_de_venta__c,
            estado__c = EXCLUDED.estado__c, enviar_a_siesa__c = EXCLUDED.enviar_a_siesa__c
        """)
    Mono<Void> upsert(SucursalEntity entity);
}
