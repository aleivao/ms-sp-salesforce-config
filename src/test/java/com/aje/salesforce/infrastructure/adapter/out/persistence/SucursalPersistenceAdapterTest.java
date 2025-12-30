package com.aje.salesforce.infrastructure.adapter.out.persistence;

import com.aje.salesforce.domain.model.Sucursal;
import com.aje.salesforce.infrastructure.adapter.out.persistence.entity.SucursalEntity;
import com.aje.salesforce.infrastructure.adapter.out.persistence.mapper.SucursalEntityMapper;
import com.aje.salesforce.infrastructure.adapter.out.persistence.repository.SucursalRepository;
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
@DisplayName("SucursalPersistenceAdapter Tests")
class SucursalPersistenceAdapterTest {

    @Mock
    private SucursalRepository sucursalRepository;

    @Mock
    private SucursalEntityMapper mapper;

    @InjectMocks
    private SucursalPersistenceAdapter adapter;

    private Sucursal testSucursal;
    private SucursalEntity testEntity;

    @BeforeEach
    void setUp() {
        testSucursal = Sucursal.builder()
                .id("a0Y5f000000AbCdEFG")
                .name("Sucursal Lima Norte")
                .codigo("SUC001")
                .estado("Activo")
                .pais("Peru")
                .isDeleted(false)
                .createdDate(LocalDateTime.now())
                .build();

        testEntity = SucursalEntity.builder()
                .id("a0Y5f000000AbCdEFG")
                .name("Sucursal Lima Norte")
                .codigo("SUC001")
                .estado("Activo")
                .pais("Peru")
                .isDeleted(false)
                .createdDate(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Should save sucursal using upsert")
    void shouldSaveSucursalUsingUpsert() {
        when(mapper.toEntity(testSucursal)).thenReturn(testEntity);
        when(sucursalRepository.upsert(testEntity)).thenReturn(Mono.empty());

        StepVerifier.create(adapter.save(testSucursal))
                .expectNext(testSucursal)
                .verifyComplete();

        verify(mapper, times(1)).toEntity(testSucursal);
        verify(sucursalRepository, times(1)).upsert(testEntity);
    }

    @Test
    @DisplayName("Should find sucursal by ID")
    void shouldFindSucursalById() {
        when(sucursalRepository.findById("a0Y5f000000AbCdEFG")).thenReturn(Mono.just(testEntity));
        when(mapper.toDomain(testEntity)).thenReturn(testSucursal);

        StepVerifier.create(adapter.findById("a0Y5f000000AbCdEFG"))
                .expectNext(testSucursal)
                .verifyComplete();

        verify(sucursalRepository, times(1)).findById("a0Y5f000000AbCdEFG");
        verify(mapper, times(1)).toDomain(testEntity);
    }

    @Test
    @DisplayName("Should return empty when sucursal not found by ID")
    void shouldReturnEmptyWhenSucursalNotFoundById() {
        when(sucursalRepository.findById("unknown-id")).thenReturn(Mono.empty());

        StepVerifier.create(adapter.findById("unknown-id"))
                .verifyComplete();

        verify(sucursalRepository, times(1)).findById("unknown-id");
        verify(mapper, never()).toDomain(any());
    }
}
