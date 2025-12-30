package com.aje.salesforce.application.port.in;

import com.aje.salesforce.domain.model.Sucursal;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SyncSucursalUseCase {

    Flux<Sucursal> syncByPais(String pais);

    Mono<Sucursal> syncById(String id);
}
