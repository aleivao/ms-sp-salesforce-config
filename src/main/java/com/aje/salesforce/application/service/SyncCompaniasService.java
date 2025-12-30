package com.aje.salesforce.application.service;

import com.aje.salesforce.application.port.in.SyncCompaniasUseCase;
import com.aje.salesforce.application.port.out.CompaniaPersistencePort;
import com.aje.salesforce.application.port.out.SalesforcePort;
import com.aje.salesforce.domain.model.Compania;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class SyncCompaniasService implements SyncCompaniasUseCase {

    private final SalesforcePort salesforcePort;
    private final CompaniaPersistencePort persistencePort;

    @Override
    public Flux<Compania> syncByPais(String pais) {
        log.info("Starting sync for country: {}", pais);

        return Flux.fromIterable(salesforcePort.findByPais(pais))
                .flatMap(persistencePort::save)
                .doOnNext(compania -> log.debug("Synced compania: {} ({})", compania.getName(), compania.getId()))
                .doOnComplete(() -> log.info("Sync completed for country: {}", pais))
                .doOnError(error -> log.error("Error syncing companias for country {}: {}", pais, error.getMessage()));
    }

    @Override
    public Mono<Compania> syncById(String id) {
        log.info("Starting sync for compania id: {}", id);

        Compania compania = salesforcePort.findById(id);
        return persistencePort.save(compania)
                .doOnSuccess(saved -> log.info("Synced compania: {} ({})", saved.getName(), saved.getId()))
                .doOnError(error -> log.error("Error syncing compania {}: {}", id, error.getMessage()));
    }
}
