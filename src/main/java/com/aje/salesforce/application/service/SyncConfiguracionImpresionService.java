package com.aje.salesforce.application.service;

import com.aje.salesforce.application.port.in.SyncConfiguracionImpresionUseCase;
import com.aje.salesforce.application.port.out.ConfiguracionImpresionPersistencePort;
import com.aje.salesforce.application.port.out.ConfiguracionImpresionSalesforcePort;
import com.aje.salesforce.domain.model.ConfiguracionImpresion;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class SyncConfiguracionImpresionService implements SyncConfiguracionImpresionUseCase {

    private final ConfiguracionImpresionSalesforcePort configuracionImpresionSalesforcePort;
    private final ConfiguracionImpresionPersistencePort persistencePort;

    @Override
    public Mono<ConfiguracionImpresion> syncById(String id) {
        log.info("Starting sync for configuracion impresion id: {}", id);

        ConfiguracionImpresion configuracion = configuracionImpresionSalesforcePort.findById(id);
        return persistencePort.save(configuracion)
                .doOnSuccess(saved -> log.info("Synced configuracion impresion: {} ({})", saved.getName(), saved.getId()))
                .doOnError(error -> log.error("Error syncing configuracion impresion {}: {}", id, error.getMessage()));
    }
}
