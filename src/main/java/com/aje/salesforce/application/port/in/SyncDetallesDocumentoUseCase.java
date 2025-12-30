package com.aje.salesforce.application.port.in;

import com.aje.salesforce.domain.model.DetallesDocumento;
import reactor.core.publisher.Mono;

public interface SyncDetallesDocumentoUseCase {
    Mono<DetallesDocumento> syncById(String id);
}
