package com.aje.salesforce.application.service;

import com.aje.salesforce.application.port.in.SyncSucursalUseCase;
import com.aje.salesforce.application.port.out.SucursalPersistencePort;
import com.aje.salesforce.application.port.out.SucursalSalesforcePort;
import com.aje.salesforce.domain.model.Sucursal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class SyncSucursalService implements SyncSucursalUseCase {

    private final SucursalSalesforcePort sucursalSalesforcePort;
    private final SucursalPersistencePort persistencePort;

    @Override
    public Flux<Sucursal> syncByPais(String pais) {
        log.info("Starting sync for sucursales in country: {}", pais);

        return Flux.fromIterable(sucursalSalesforcePort.findByPais(pais))
                .flatMap(persistencePort::save)
                .doOnNext(sucursal -> log.debug("Synced sucursal: {} ({})", sucursal.getName(), sucursal.getId()))
                .doOnComplete(() -> log.info("Sync completed for country: {}", pais))
                .doOnError(error -> log.error("Error syncing sucursales for country {}: {}", pais, error.getMessage()));
    }

    @Override
    public Mono<Sucursal> syncById(String id) {
        log.info("Starting sync for sucursal id: {}", id);

        Sucursal sucursal = sucursalSalesforcePort.findById(id);
        return persistencePort.save(sucursal)
                .doOnSuccess(saved -> log.info("Synced sucursal: {} ({})", saved.getName(), saved.getId()))
                .doOnError(error -> log.error("Error syncing sucursal {}: {}", id, error.getMessage()));
    }
}
