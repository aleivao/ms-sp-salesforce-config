package com.aje.salesforce.application.service;

import com.aje.salesforce.application.port.in.SyncTipoDocumentoUseCase;
import com.aje.salesforce.application.port.out.TipoDocumentoPersistencePort;
import com.aje.salesforce.application.port.out.TipoDocumentoSalesforcePort;
import com.aje.salesforce.domain.model.TipoDocumento;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class SyncTipoDocumentoService implements SyncTipoDocumentoUseCase {

    private final TipoDocumentoSalesforcePort tipoDocumentoSalesforcePort;
    private final TipoDocumentoPersistencePort persistencePort;

    @Override
    public Mono<TipoDocumento> syncById(String id) {
        log.info("Starting sync for tipo documento id: {}", id);

        TipoDocumento tipoDocumento = tipoDocumentoSalesforcePort.findById(id);
        return persistencePort.save(tipoDocumento)
                .doOnSuccess(saved -> log.info("Synced tipo documento: {} ({})", saved.getName(), saved.getId()))
                .doOnError(error -> log.error("Error syncing tipo documento {}: {}", id, error.getMessage()));
    }
}
