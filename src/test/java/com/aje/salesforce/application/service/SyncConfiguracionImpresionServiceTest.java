package com.aje.salesforce.application.service;

import com.aje.salesforce.application.port.out.ConfiguracionImpresionPersistencePort;
import com.aje.salesforce.application.port.out.ConfiguracionImpresionSalesforcePort;
import com.aje.salesforce.domain.model.ConfiguracionImpresion;
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
@DisplayName("SyncConfiguracionImpresionService Tests")
class SyncConfiguracionImpresionServiceTest {

    @Mock
    private ConfiguracionImpresionSalesforcePort configuracionImpresionSalesforcePort;

    @Mock
    private ConfiguracionImpresionPersistencePort persistencePort;

    @InjectMocks
    private SyncConfiguracionImpresionService syncConfiguracionImpresionService;

    private ConfiguracionImpresion testConfiguracion;

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
    }

    @Test
    @DisplayName("Should sync configuracion impresion by ID")
    void shouldSyncConfiguracionImpresionById() {
        when(configuracionImpresionSalesforcePort.findById("a0X5f000000AbCdEFG")).thenReturn(testConfiguracion);
        when(persistencePort.save(any(ConfiguracionImpresion.class))).thenReturn(Mono.just(testConfiguracion));

        StepVerifier.create(syncConfiguracionImpresionService.syncById("a0X5f000000AbCdEFG"))
                .expectNext(testConfiguracion)
                .verifyComplete();

        verify(configuracionImpresionSalesforcePort, times(1)).findById("a0X5f000000AbCdEFG");
        verify(persistencePort, times(1)).save(testConfiguracion);
    }

    @Test
    @DisplayName("Should handle error when syncing configuracion impresion by ID")
    void shouldHandleErrorWhenSyncingConfiguracionImpresionById() {
        when(configuracionImpresionSalesforcePort.findById("invalid-id")).thenReturn(testConfiguracion);
        when(persistencePort.save(any(ConfiguracionImpresion.class))).thenReturn(Mono.error(new RuntimeException("DB Error")));

        StepVerifier.create(syncConfiguracionImpresionService.syncById("invalid-id"))
                .expectError(RuntimeException.class)
                .verify();

        verify(configuracionImpresionSalesforcePort, times(1)).findById("invalid-id");
        verify(persistencePort, times(1)).save(testConfiguracion);
    }
}
