package com.aje.salesforce.infrastructure.adapter.out.persistence;

import com.aje.salesforce.application.port.out.CompaniaPersistencePort;
import com.aje.salesforce.domain.model.Compania;
import com.aje.salesforce.infrastructure.adapter.out.persistence.entity.CompaniaEntity;
import com.aje.salesforce.infrastructure.adapter.out.persistence.mapper.CompaniaEntityMapper;
import com.aje.salesforce.infrastructure.adapter.out.persistence.repository.CompaniaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class CompaniaPersistenceAdapter implements CompaniaPersistencePort {

    private final CompaniaRepository companiaRepository;
    private final CompaniaEntityMapper mapper;

    @Override
    public Mono<Compania> save(Compania compania) {
        log.debug("Saving compania with id: {}", compania.getId());
        CompaniaEntity entity = mapper.toEntity(compania);
        return companiaRepository.upsert(entity)
                .then(Mono.just(compania))
                .doOnSuccess(saved -> log.debug("Compania saved successfully: {}", saved.getId()));
    }

    @Override
    public Flux<Compania> saveAll(Flux<Compania> companias) {
        return companias
                .map(mapper::toEntity)
                .collectList()
                .flatMapMany(entities -> {
                    log.debug("Saving {} companias", entities.size());
                    return companiaRepository.saveAll(entities);
                })
                .map(mapper::toDomain)
                .doOnComplete(() -> log.debug("All companias saved successfully"));
    }

    @Override
    public Mono<Compania> findById(String id) {
        log.debug("Finding compania by id: {}", id);
        return companiaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Flux<Compania> findByPais(String pais) {
        log.debug("Finding companias by pais: {}", pais);
        return companiaRepository.findByPais(pais)
                .map(mapper::toDomain);
    }

    @Override
    public Flux<Compania> findAll() {
        log.debug("Finding all companias");
        return companiaRepository.findAll()
                .map(mapper::toDomain);
    }
}
