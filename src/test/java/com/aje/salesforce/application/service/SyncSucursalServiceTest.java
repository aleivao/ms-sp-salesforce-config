package com.aje.salesforce.application.service;

import com.aje.salesforce.application.port.out.SucursalPersistencePort;
import com.aje.salesforce.application.port.out.SucursalSalesforcePort;
import com.aje.salesforce.domain.model.Sucursal;
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
@DisplayName("SyncSucursalService Tests")
class SyncSucursalServiceTest {

    @Mock
    private SucursalSalesforcePort sucursalSalesforcePort;

    @Mock
    private SucursalPersistencePort persistencePort;

    @InjectMocks
    private SyncSucursalService syncSucursalService;

    private Sucursal testSucursal;

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
    }

    @Test
    @DisplayName("Should sync sucursales by country")
    void shouldSyncSucursalesByCountry() {
        List<Sucursal> sucursales = Arrays.asList(testSucursal);
        when(sucursalSalesforcePort.findByPais("Peru")).thenReturn(sucursales);
        when(persistencePort.save(any(Sucursal.class))).thenReturn(Mono.just(testSucursal));

        StepVerifier.create(syncSucursalService.syncByPais("Peru"))
                .expectNext(testSucursal)
                .verifyComplete();

        verify(sucursalSalesforcePort, times(1)).findByPais("Peru");
        verify(persistencePort, times(1)).save(any(Sucursal.class));
    }

    @Test
    @DisplayName("Should return empty when no sucursales to sync")
    void shouldReturnEmptyWhenNoSucursalesToSync() {
        when(sucursalSalesforcePort.findByPais("Unknown")).thenReturn(Collections.emptyList());

        StepVerifier.create(syncSucursalService.syncByPais("Unknown"))
                .verifyComplete();

        verify(sucursalSalesforcePort, times(1)).findByPais("Unknown");
        verify(persistencePort, never()).save(any(Sucursal.class));
    }

    @Test
    @DisplayName("Should sync multiple sucursales by country")
    void shouldSyncMultipleSucursalesByCountry() {
        Sucursal sucursal2 = Sucursal.builder()
                .id("a0Y5f000000XyZaBC")
                .name("Sucursal Lima Sur")
                .codigo("SUC002")
                .estado("Activo")
                .pais("Peru")
                .isDeleted(false)
                .build();

        List<Sucursal> sucursales = Arrays.asList(testSucursal, sucursal2);
        when(sucursalSalesforcePort.findByPais("Peru")).thenReturn(sucursales);
        when(persistencePort.save(testSucursal)).thenReturn(Mono.just(testSucursal));
        when(persistencePort.save(sucursal2)).thenReturn(Mono.just(sucursal2));

        StepVerifier.create(syncSucursalService.syncByPais("Peru"))
                .expectNext(testSucursal)
                .expectNext(sucursal2)
                .verifyComplete();

        verify(sucursalSalesforcePort, times(1)).findByPais("Peru");
        verify(persistencePort, times(2)).save(any(Sucursal.class));
    }

    @Test
    @DisplayName("Should sync sucursal by ID")
    void shouldSyncSucursalById() {
        when(sucursalSalesforcePort.findById("a0Y5f000000AbCdEFG")).thenReturn(testSucursal);
        when(persistencePort.save(any(Sucursal.class))).thenReturn(Mono.just(testSucursal));

        StepVerifier.create(syncSucursalService.syncById("a0Y5f000000AbCdEFG"))
                .expectNext(testSucursal)
                .verifyComplete();

        verify(sucursalSalesforcePort, times(1)).findById("a0Y5f000000AbCdEFG");
        verify(persistencePort, times(1)).save(testSucursal);
    }

    @Test
    @DisplayName("Should handle error when syncing sucursal by ID")
    void shouldHandleErrorWhenSyncingSucursalById() {
        when(sucursalSalesforcePort.findById("invalid-id")).thenReturn(testSucursal);
        when(persistencePort.save(any(Sucursal.class))).thenReturn(Mono.error(new RuntimeException("DB Error")));

        StepVerifier.create(syncSucursalService.syncById("invalid-id"))
                .expectError(RuntimeException.class)
                .verify();

        verify(sucursalSalesforcePort, times(1)).findById("invalid-id");
        verify(persistencePort, times(1)).save(testSucursal);
    }
}
