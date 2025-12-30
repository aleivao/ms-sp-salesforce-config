package com.aje.salesforce.infrastructure.adapter.out.persistence;

import com.aje.salesforce.domain.model.Compania;
import com.aje.salesforce.infrastructure.adapter.out.persistence.entity.CompaniaEntity;
import com.aje.salesforce.infrastructure.adapter.out.persistence.mapper.CompaniaEntityMapper;
import com.aje.salesforce.infrastructure.adapter.out.persistence.repository.CompaniaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CompaniaPersistenceAdapter Tests")
class CompaniaPersistenceAdapterTest {

    @Mock
    private CompaniaRepository companiaRepository;

    @Mock
    private CompaniaEntityMapper mapper;

    @InjectMocks
    private CompaniaPersistenceAdapter adapter;

    private Compania testCompania;
    private CompaniaEntity testEntity;

    @BeforeEach
    void setUp() {
        testCompania = Compania.builder()
                .id("a0X5f000000AbCdEFG")
                .name("AJE PERU")
                .codigo("PE001")
                .estado("Activo")
                .pais("Peru")
                .isDeleted(false)
                .createdDate(LocalDateTime.now())
                .build();

        testEntity = CompaniaEntity.builder()
                .id("a0X5f000000AbCdEFG")
                .name("AJE PERU")
                .codigo("PE001")
                .estado("Activo")
                .pais("Peru")
                .isDeleted(false)
                .createdDate(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Should save compania using upsert")
    void shouldSaveCompaniaUsingUpsert() {
        when(mapper.toEntity(testCompania)).thenReturn(testEntity);
        when(companiaRepository.upsert(testEntity)).thenReturn(Mono.empty());

        StepVerifier.create(adapter.save(testCompania))
                .expectNext(testCompania)
                .verifyComplete();

        verify(mapper, times(1)).toEntity(testCompania);
        verify(companiaRepository, times(1)).upsert(testEntity);
    }

    @Test
    @DisplayName("Should find compania by ID")
    void shouldFindCompaniaById() {
        when(companiaRepository.findById("a0X5f000000AbCdEFG")).thenReturn(Mono.just(testEntity));
        when(mapper.toDomain(testEntity)).thenReturn(testCompania);

        StepVerifier.create(adapter.findById("a0X5f000000AbCdEFG"))
                .expectNext(testCompania)
                .verifyComplete();

        verify(companiaRepository, times(1)).findById("a0X5f000000AbCdEFG");
        verify(mapper, times(1)).toDomain(testEntity);
    }

    @Test
    @DisplayName("Should return empty when compania not found by ID")
    void shouldReturnEmptyWhenCompaniaNotFoundById() {
        when(companiaRepository.findById("unknown-id")).thenReturn(Mono.empty());

        StepVerifier.create(adapter.findById("unknown-id"))
                .verifyComplete();

        verify(companiaRepository, times(1)).findById("unknown-id");
        verify(mapper, never()).toDomain(any());
    }

    @Test
    @DisplayName("Should find companias by country")
    void shouldFindCompaniasByCountry() {
        when(companiaRepository.findByPais("Peru")).thenReturn(Flux.just(testEntity));
        when(mapper.toDomain(testEntity)).thenReturn(testCompania);

        StepVerifier.create(adapter.findByPais("Peru"))
                .expectNext(testCompania)
                .verifyComplete();

        verify(companiaRepository, times(1)).findByPais("Peru");
        verify(mapper, times(1)).toDomain(testEntity);
    }

    @Test
    @DisplayName("Should return empty flux when no companias found by country")
    void shouldReturnEmptyFluxWhenNoCompaniasFoundByCountry() {
        when(companiaRepository.findByPais("Unknown")).thenReturn(Flux.empty());

        StepVerifier.create(adapter.findByPais("Unknown"))
                .verifyComplete();

        verify(companiaRepository, times(1)).findByPais("Unknown");
        verify(mapper, never()).toDomain(any());
    }

    @Test
    @DisplayName("Should find all companias")
    void shouldFindAllCompanias() {
        when(companiaRepository.findAll()).thenReturn(Flux.just(testEntity));
        when(mapper.toDomain(testEntity)).thenReturn(testCompania);

        StepVerifier.create(adapter.findAll())
                .expectNext(testCompania)
                .verifyComplete();

        verify(companiaRepository, times(1)).findAll();
        verify(mapper, times(1)).toDomain(testEntity);
    }
}
