package com.aje.salesforce.infrastructure.adapter.out.persistence;

import com.aje.salesforce.domain.model.TipoDocumento;
import com.aje.salesforce.infrastructure.adapter.out.persistence.entity.TipoDocumentoEntity;
import com.aje.salesforce.infrastructure.adapter.out.persistence.mapper.TipoDocumentoEntityMapper;
import com.aje.salesforce.infrastructure.adapter.out.persistence.repository.TipoDocumentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TipoDocumentoPersistenceAdapter Tests")
class TipoDocumentoPersistenceAdapterTest {

    @Mock
    private TipoDocumentoRepository tipoDocumentoRepository;

    @Mock
    private TipoDocumentoEntityMapper mapper;

    @InjectMocks
    private TipoDocumentoPersistenceAdapter adapter;

    private TipoDocumento testTipoDocumento;
    private TipoDocumentoEntity testEntity;

    @BeforeEach
    void setUp() {
        testTipoDocumento = TipoDocumento.builder()
                .id("a0X5f000000AbCdEFG")
                .name("DNI")
                .pais("PE")
                .codigo("DNI")
                .activo(true)
                .isDeleted(false)
                .longitud(8.0)
                .createdDate(LocalDateTime.now())
                .build();

        testEntity = TipoDocumentoEntity.builder()
                .id("a0X5f000000AbCdEFG")
                .name("DNI")
                .pais("PE")
                .codigo("DNI")
                .activo(true)
                .isDeleted(false)
                .longitud(8.0)
                .createdDate(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Should save tipo documento successfully")
    void shouldSaveTipoDocumentoSuccessfully() {
        when(mapper.toEntity(testTipoDocumento)).thenReturn(testEntity);
        when(tipoDocumentoRepository.upsert(testEntity)).thenReturn(Mono.empty());

        StepVerifier.create(adapter.save(testTipoDocumento))
                .expectNext(testTipoDocumento)
                .verifyComplete();

        verify(mapper, times(1)).toEntity(testTipoDocumento);
        verify(tipoDocumentoRepository, times(1)).upsert(testEntity);
    }

    @Test
    @DisplayName("Should handle error when saving tipo documento")
    void shouldHandleErrorWhenSavingTipoDocumento() {
        when(mapper.toEntity(testTipoDocumento)).thenReturn(testEntity);
        when(tipoDocumentoRepository.upsert(testEntity)).thenReturn(Mono.error(new RuntimeException("DB Error")));

        StepVerifier.create(adapter.save(testTipoDocumento))
                .expectError(RuntimeException.class)
                .verify();

        verify(mapper, times(1)).toEntity(testTipoDocumento);
        verify(tipoDocumentoRepository, times(1)).upsert(testEntity);
    }

    @Test
    @DisplayName("Should find tipo documento by ID")
    void shouldFindTipoDocumentoById() {
        when(tipoDocumentoRepository.findById("a0X5f000000AbCdEFG")).thenReturn(Mono.just(testEntity));
        when(mapper.toDomain(testEntity)).thenReturn(testTipoDocumento);

        StepVerifier.create(adapter.findById("a0X5f000000AbCdEFG"))
                .expectNext(testTipoDocumento)
                .verifyComplete();

        verify(tipoDocumentoRepository, times(1)).findById("a0X5f000000AbCdEFG");
        verify(mapper, times(1)).toDomain(testEntity);
    }

    @Test
    @DisplayName("Should return empty when tipo documento not found")
    void shouldReturnEmptyWhenTipoDocumentoNotFound() {
        when(tipoDocumentoRepository.findById("not-found")).thenReturn(Mono.empty());

        StepVerifier.create(adapter.findById("not-found"))
                .verifyComplete();

        verify(tipoDocumentoRepository, times(1)).findById("not-found");
        verify(mapper, never()).toDomain(any());
    }
}
