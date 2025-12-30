package com.aje.salesforce.application.service;

import com.aje.salesforce.application.port.out.CompaniaPersistencePort;
import com.aje.salesforce.application.port.out.SalesforcePort;
import com.aje.salesforce.domain.model.Compania;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SyncCompaniasService Tests")
class SyncCompaniasServiceTest {

    @Mock
    private SalesforcePort salesforcePort;

    @Mock
    private CompaniaPersistencePort persistencePort;

    @InjectMocks
    private SyncCompaniasService syncCompaniasService;

    private Compania testCompania;

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
    }

    @Test
    @DisplayName("Should sync companies by country")
    void shouldSyncCompaniesByCountry() {
        List<Compania> companias = Arrays.asList(testCompania);
        when(salesforcePort.findByPais("Peru")).thenReturn(companias);
        when(persistencePort.save(any(Compania.class))).thenReturn(Mono.just(testCompania));

        StepVerifier.create(syncCompaniasService.syncByPais("Peru"))
                .expectNext(testCompania)
                .verifyComplete();

        verify(salesforcePort, times(1)).findByPais("Peru");
        verify(persistencePort, times(1)).save(any(Compania.class));
    }

    @Test
    @DisplayName("Should return empty when no companies to sync")
    void shouldReturnEmptyWhenNoCompaniesToSync() {
        when(salesforcePort.findByPais("Unknown")).thenReturn(Collections.emptyList());

        StepVerifier.create(syncCompaniasService.syncByPais("Unknown"))
                .verifyComplete();

        verify(salesforcePort, times(1)).findByPais("Unknown");
        verify(persistencePort, never()).save(any(Compania.class));
    }

    @Test
    @DisplayName("Should sync company by ID")
    void shouldSyncCompanyById() {
        when(salesforcePort.findById("a0X5f000000AbCdEFG")).thenReturn(testCompania);
        when(persistencePort.save(any(Compania.class))).thenReturn(Mono.just(testCompania));

        StepVerifier.create(syncCompaniasService.syncById("a0X5f000000AbCdEFG"))
                .expectNext(testCompania)
                .verifyComplete();

        verify(salesforcePort, times(1)).findById("a0X5f000000AbCdEFG");
        verify(persistencePort, times(1)).save(testCompania);
    }

    @Test
    @DisplayName("Should handle error when syncing by ID")
    void shouldHandleErrorWhenSyncingById() {
        when(salesforcePort.findById("invalid-id")).thenReturn(testCompania);
        when(persistencePort.save(any(Compania.class))).thenReturn(Mono.error(new RuntimeException("DB Error")));

        StepVerifier.create(syncCompaniasService.syncById("invalid-id"))
                .expectError(RuntimeException.class)
                .verify();

        verify(salesforcePort, times(1)).findById("invalid-id");
        verify(persistencePort, times(1)).save(testCompania);
    }

    @Test
    @DisplayName("Should sync multiple companies by country")
    void shouldSyncMultipleCompaniesByCountry() {
        Compania compania2 = Compania.builder()
                .id("a0X5f000000XyZaBC")
                .name("AJE LIMA")
                .codigo("PE002")
                .estado("Activo")
                .pais("Peru")
                .isDeleted(false)
                .build();

        List<Compania> companias = Arrays.asList(testCompania, compania2);
        when(salesforcePort.findByPais("Peru")).thenReturn(companias);
        when(persistencePort.save(testCompania)).thenReturn(Mono.just(testCompania));
        when(persistencePort.save(compania2)).thenReturn(Mono.just(compania2));

        StepVerifier.create(syncCompaniasService.syncByPais("Peru"))
                .expectNext(testCompania)
                .expectNext(compania2)
                .verifyComplete();

        verify(salesforcePort, times(1)).findByPais("Peru");
        verify(persistencePort, times(2)).save(any(Compania.class));
    }
}
