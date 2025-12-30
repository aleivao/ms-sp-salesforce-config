package com.aje.salesforce.infrastructure.adapter.out.persistence;

import com.aje.salesforce.domain.model.ConfiguracionImpresion;
import com.aje.salesforce.infrastructure.adapter.out.persistence.entity.ConfiguracionImpresionEntity;
import com.aje.salesforce.infrastructure.adapter.out.persistence.mapper.ConfiguracionImpresionEntityMapper;
import com.aje.salesforce.infrastructure.adapter.out.persistence.repository.ConfiguracionImpresionRepository;
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
@DisplayName("ConfiguracionImpresionPersistenceAdapter Tests")
class ConfiguracionImpresionPersistenceAdapterTest {

    @Mock
    private ConfiguracionImpresionRepository configuracionImpresionRepository;

    @Mock
    private ConfiguracionImpresionEntityMapper mapper;

    @InjectMocks
    private ConfiguracionImpresionPersistenceAdapter adapter;

    private ConfiguracionImpresion testConfiguracion;
    private ConfiguracionImpresionEntity testEntity;

    @BeforeEach
    void setUp() {
        testConfiguracion = ConfiguracionImpresion.builder()
                .id("a0X5f000000AbCdEFG")
                .name("Config Factura PE")
                .pais("PE")
                .tipoDocumento("Factura")
                .documentoDefault(true)
                .isDeleted(false)
                .createdDate(LocalDateTime.now())
                .build();

        testEntity = ConfiguracionImpresionEntity.builder()
                .id("a0X5f000000AbCdEFG")
                .name("Config Factura PE")
                .pais("PE")
                .tipoDocumento("Factura")
                .documentoDefault(true)
                .isDeleted(false)
                .createdDate(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Should save configuracion impresion successfully")
    void shouldSaveConfiguracionImpresionSuccessfully() {
        when(mapper.toEntity(testConfiguracion)).thenReturn(testEntity);
        when(configuracionImpresionRepository.upsert(testEntity)).thenReturn(Mono.empty());

        StepVerifier.create(adapter.save(testConfiguracion))
                .expectNext(testConfiguracion)
                .verifyComplete();

        verify(mapper, times(1)).toEntity(testConfiguracion);
        verify(configuracionImpresionRepository, times(1)).upsert(testEntity);
    }

    @Test
    @DisplayName("Should find configuracion impresion by ID")
    void shouldFindConfiguracionImpresionById() {
        when(configuracionImpresionRepository.findById("a0X5f000000AbCdEFG")).thenReturn(Mono.just(testEntity));
        when(mapper.toDomain(testEntity)).thenReturn(testConfiguracion);

        StepVerifier.create(adapter.findById("a0X5f000000AbCdEFG"))
                .expectNext(testConfiguracion)
                .verifyComplete();

        verify(configuracionImpresionRepository, times(1)).findById("a0X5f000000AbCdEFG");
        verify(mapper, times(1)).toDomain(testEntity);
    }

    @Test
    @DisplayName("Should return empty when configuracion impresion not found")
    void shouldReturnEmptyWhenConfiguracionImpresionNotFound() {
        when(configuracionImpresionRepository.findById("not-found")).thenReturn(Mono.empty());

        StepVerifier.create(adapter.findById("not-found"))
                .verifyComplete();

        verify(configuracionImpresionRepository, times(1)).findById("not-found");
        verify(mapper, never()).toDomain(any());
    }
}
