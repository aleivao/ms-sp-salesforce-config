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
