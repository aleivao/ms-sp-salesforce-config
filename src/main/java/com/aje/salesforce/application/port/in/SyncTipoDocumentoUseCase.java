package com.aje.salesforce.application.port.in;

import com.aje.salesforce.domain.model.TipoDocumento;
import reactor.core.publisher.Mono;

public interface SyncTipoDocumentoUseCase {

    Mono<TipoDocumento> syncById(String id);
}
