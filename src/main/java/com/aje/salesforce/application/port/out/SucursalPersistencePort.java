package com.aje.salesforce.application.port.out;

import com.aje.salesforce.domain.model.Sucursal;
import reactor.core.publisher.Mono;

public interface SucursalPersistencePort {

    Mono<Sucursal> save(Sucursal sucursal);

    Mono<Sucursal> findById(String id);
}
