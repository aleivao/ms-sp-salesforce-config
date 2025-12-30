package com.aje.salesforce.infrastructure.adapter.out.persistence;

import com.aje.salesforce.application.port.out.DetallesDocumentoPersistencePort;
import com.aje.salesforce.domain.model.DetallesDocumento;
import com.aje.salesforce.infrastructure.adapter.out.persistence.mapper.DetallesDocumentoEntityMapper;
import com.aje.salesforce.infrastructure.adapter.out.persistence.repository.DetallesDocumentoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class DetallesDocumentoPersistenceAdapter implements DetallesDocumentoPersistencePort {

    private final DetallesDocumentoRepository repository;
    private final DetallesDocumentoEntityMapper mapper;

    @Override
    public Mono<DetallesDocumento> save(DetallesDocumento detallesDocumento) {
        log.info("Saving detalles documento: {} ({})", detallesDocumento.getName(), detallesDocumento.getId());

        return repository.upsert(mapper.toEntity(detallesDocumento))
                .map(mapper::toDomain)
                .doOnSuccess(saved -> log.info("Saved detalles documento: {} ({})", saved.getName(), saved.getId()))
                .doOnError(error -> log.error("Error saving detalles documento {}: {}", detallesDocumento.getId(), error.getMessage()));
    }
}
