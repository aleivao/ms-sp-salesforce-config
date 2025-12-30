package com.aje.salesforce.application.port.in;

import com.aje.salesforce.domain.model.Compania;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SyncCompaniasUseCase {

    Flux<Compania> syncByPais(String pais);

    Mono<Compania> syncById(String id);
}
