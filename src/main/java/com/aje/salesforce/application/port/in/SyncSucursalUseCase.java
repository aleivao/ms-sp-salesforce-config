package com.aje.salesforce.application.port.in;

import com.aje.salesforce.domain.model.Sucursal;
import reactor.core.publisher.Mono;

public interface SyncSucursalUseCase {

    Mono<Sucursal> syncById(String id);
}
