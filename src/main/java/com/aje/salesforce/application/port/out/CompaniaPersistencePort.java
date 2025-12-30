package com.aje.salesforce.application.port.out;

import com.aje.salesforce.domain.model.Compania;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CompaniaPersistencePort {

    Mono<Compania> save(Compania compania);

    Flux<Compania> saveAll(Flux<Compania> companias);

    Mono<Compania> findById(String id);

    Flux<Compania> findByPais(String pais);

    Flux<Compania> findAll();
}
