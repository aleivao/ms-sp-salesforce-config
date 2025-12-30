package com.aje.salesforce.application.port.out;

import com.aje.salesforce.domain.model.ConfiguracionImpresion;
import reactor.core.publisher.Mono;

public interface ConfiguracionImpresionPersistencePort {

    Mono<ConfiguracionImpresion> save(ConfiguracionImpresion configuracionImpresion);

    Mono<ConfiguracionImpresion> findById(String id);
}
