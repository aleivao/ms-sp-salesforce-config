package com.aje.salesforce.infrastructure.adapter.out.persistence;

import com.aje.salesforce.application.port.out.ConfiguracionImpresionPersistencePort;
import com.aje.salesforce.domain.model.ConfiguracionImpresion;
import com.aje.salesforce.infrastructure.adapter.out.persistence.entity.ConfiguracionImpresionEntity;
import com.aje.salesforce.infrastructure.adapter.out.persistence.mapper.ConfiguracionImpresionEntityMapper;
import com.aje.salesforce.infrastructure.adapter.out.persistence.repository.ConfiguracionImpresionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConfiguracionImpresionPersistenceAdapter implements ConfiguracionImpresionPersistencePort {

    private final ConfiguracionImpresionRepository configuracionImpresionRepository;
    private final ConfiguracionImpresionEntityMapper mapper;

    @Override
    public Mono<ConfiguracionImpresion> save(ConfiguracionImpresion configuracionImpresion) {
        log.debug("Saving configuracion impresion with id: {}", configuracionImpresion.getId());
        ConfiguracionImpresionEntity entity = mapper.toEntity(configuracionImpresion);
        return configuracionImpresionRepository.upsert(entity)
                .then(Mono.just(configuracionImpresion))
                .doOnSuccess(saved -> log.debug("Configuracion impresion saved successfully: {}", saved.getId()));
    }

    @Override
    public Mono<ConfiguracionImpresion> findById(String id) {
        log.debug("Finding configuracion impresion by id: {}", id);
        return configuracionImpresionRepository.findById(id)
                .map(mapper::toDomain);
    }
}
