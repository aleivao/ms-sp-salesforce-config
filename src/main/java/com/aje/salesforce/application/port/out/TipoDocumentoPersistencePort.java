package com.aje.salesforce.application.port.out;

import com.aje.salesforce.domain.model.TipoDocumento;
import reactor.core.publisher.Mono;

public interface TipoDocumentoPersistencePort {

    Mono<TipoDocumento> save(TipoDocumento tipoDocumento);

    Mono<TipoDocumento> findById(String id);
}
