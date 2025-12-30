package com.aje.salesforce.infrastructure.adapter.out.persistence;

import com.aje.salesforce.application.port.out.TipoDocumentoPersistencePort;
import com.aje.salesforce.domain.model.TipoDocumento;
import com.aje.salesforce.infrastructure.adapter.out.persistence.entity.TipoDocumentoEntity;
import com.aje.salesforce.infrastructure.adapter.out.persistence.mapper.TipoDocumentoEntityMapper;
import com.aje.salesforce.infrastructure.adapter.out.persistence.repository.TipoDocumentoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class TipoDocumentoPersistenceAdapter implements TipoDocumentoPersistencePort {

    private final TipoDocumentoRepository tipoDocumentoRepository;
    private final TipoDocumentoEntityMapper mapper;

    @Override
    public Mono<TipoDocumento> save(TipoDocumento tipoDocumento) {
        log.debug("Saving tipo documento with id: {}", tipoDocumento.getId());
        TipoDocumentoEntity entity = mapper.toEntity(tipoDocumento);
        return tipoDocumentoRepository.upsert(entity)
                .then(Mono.just(tipoDocumento))
                .doOnSuccess(saved -> log.debug("Tipo documento saved successfully: {}", saved.getId()));
    }

    @Override
    public Mono<TipoDocumento> findById(String id) {
        log.debug("Finding tipo documento by id: {}", id);
        return tipoDocumentoRepository.findById(id)
                .map(mapper::toDomain);
    }
}
