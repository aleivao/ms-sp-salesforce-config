package com.aje.salesforce.infrastructure.adapter.out.persistence;

import com.aje.salesforce.application.port.out.SucursalPersistencePort;
import com.aje.salesforce.domain.model.Sucursal;
import com.aje.salesforce.infrastructure.adapter.out.persistence.entity.SucursalEntity;
import com.aje.salesforce.infrastructure.adapter.out.persistence.mapper.SucursalEntityMapper;
import com.aje.salesforce.infrastructure.adapter.out.persistence.repository.SucursalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class SucursalPersistenceAdapter implements SucursalPersistencePort {

    private final SucursalRepository sucursalRepository;
    private final SucursalEntityMapper mapper;

    @Override
    public Mono<Sucursal> save(Sucursal sucursal) {
        log.debug("Saving sucursal with id: {}", sucursal.getId());
        SucursalEntity entity = mapper.toEntity(sucursal);
        return sucursalRepository.upsert(entity)
                .then(Mono.just(sucursal))
                .doOnSuccess(saved -> log.debug("Sucursal saved successfully: {}", saved.getId()));
    }

    @Override
    public Mono<Sucursal> findById(String id) {
        log.debug("Finding sucursal by id: {}", id);
        return sucursalRepository.findById(id)
                .map(mapper::toDomain);
    }
}
