package com.aje.salesforce.application.port.in;

import com.aje.salesforce.domain.model.ConfiguracionImpresion;
import reactor.core.publisher.Mono;

public interface SyncConfiguracionImpresionUseCase {

    Mono<ConfiguracionImpresion> syncById(String id);
}
