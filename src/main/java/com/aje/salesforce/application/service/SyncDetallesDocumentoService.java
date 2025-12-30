package com.aje.salesforce.application.service;

import com.aje.salesforce.application.port.in.SyncDetallesDocumentoUseCase;
import com.aje.salesforce.application.port.out.DetallesDocumentoPersistencePort;
import com.aje.salesforce.application.port.out.DetallesDocumentoSalesforcePort;
import com.aje.salesforce.domain.model.DetallesDocumento;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class SyncDetallesDocumentoService implements SyncDetallesDocumentoUseCase {

    private final DetallesDocumentoSalesforcePort salesforcePort;
    private final DetallesDocumentoPersistencePort persistencePort;

    @Override
    public Mono<DetallesDocumento> syncById(String id) {
        log.info("Starting sync for detalles documento id: {}", id);

        DetallesDocumento detallesDocumento = salesforcePort.findById(id);
        return persistencePort.save(detallesDocumento)
                .doOnSuccess(saved -> log.info("Synced detalles documento: {} ({})", saved.getName(), saved.getId()))
                .doOnError(error -> log.error("Error syncing detalles documento {}: {}", id, error.getMessage()));
    }
}
