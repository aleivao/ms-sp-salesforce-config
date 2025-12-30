package com.aje.salesforce.infrastructure.adapter.out.persistence;

import com.aje.salesforce.domain.model.DetallesDocumento;
import com.aje.salesforce.infrastructure.adapter.out.persistence.entity.DetallesDocumentoEntity;
import com.aje.salesforce.infrastructure.adapter.out.persistence.mapper.DetallesDocumentoEntityMapper;
import com.aje.salesforce.infrastructure.adapter.out.persistence.repository.DetallesDocumentoRepository;
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
@DisplayName("DetallesDocumentoPersistenceAdapter Tests")
class DetallesDocumentoPersistenceAdapterTest {

    @Mock
    private DetallesDocumentoRepository detallesDocumentoRepository;

    @Mock
    private DetallesDocumentoEntityMapper mapper;

    @InjectMocks
    private DetallesDocumentoPersistenceAdapter adapter;

    private DetallesDocumento testDetallesDocumento;
    private DetallesDocumentoEntity testEntity;

    @BeforeEach
    void setUp() {
        testDetallesDocumento = DetallesDocumento.builder()
                .id("a0X5f000000AbCdEFG")
                .name("Detalle 001")
                .configuracionDeImpresion("a0X5f000000Config1")
                .documentoDeVenta("Factura")
                .texto1("Texto de prueba")
                .isDeleted(false)
                .createdDate(LocalDateTime.now())
                .build();

        testEntity = DetallesDocumentoEntity.builder()
                .id("a0X5f000000AbCdEFG")
                .name("Detalle 001")
                .configuracionDeImpresion("a0X5f000000Config1")
                .documentoDeVenta("Factura")
                .texto1("Texto de prueba")
                .isDeleted(false)
                .createdDate(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Should save detalles documento successfully")
    void shouldSaveDetallesDocumentoSuccessfully() {
        when(mapper.toEntity(testDetallesDocumento)).thenReturn(testEntity);
        when(detallesDocumentoRepository.upsert(testEntity)).thenReturn(Mono.just(testEntity));
        when(mapper.toDomain(testEntity)).thenReturn(testDetallesDocumento);

        StepVerifier.create(adapter.save(testDetallesDocumento))
                .expectNext(testDetallesDocumento)
                .verifyComplete();

        verify(mapper, times(1)).toEntity(testDetallesDocumento);
        verify(detallesDocumentoRepository, times(1)).upsert(testEntity);
        verify(mapper, times(1)).toDomain(testEntity);
    }

    @Test
    @DisplayName("Should handle error when saving detalles documento")
    void shouldHandleErrorWhenSavingDetallesDocumento() {
        when(mapper.toEntity(testDetallesDocumento)).thenReturn(testEntity);
        when(detallesDocumentoRepository.upsert(testEntity)).thenReturn(Mono.error(new RuntimeException("DB Error")));

        StepVerifier.create(adapter.save(testDetallesDocumento))
                .expectError(RuntimeException.class)
                .verify();

        verify(mapper, times(1)).toEntity(testDetallesDocumento);
        verify(detallesDocumentoRepository, times(1)).upsert(testEntity);
    }
}
