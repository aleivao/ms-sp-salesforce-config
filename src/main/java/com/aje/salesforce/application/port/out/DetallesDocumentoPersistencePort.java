package com.aje.salesforce.application.port.out;

import com.aje.salesforce.domain.model.DetallesDocumento;
import reactor.core.publisher.Mono;

public interface DetallesDocumentoPersistencePort {
    Mono<DetallesDocumento> save(DetallesDocumento detallesDocumento);
}
