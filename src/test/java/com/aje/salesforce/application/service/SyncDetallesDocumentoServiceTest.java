package com.aje.salesforce.application.service;

import com.aje.salesforce.application.port.out.DetallesDocumentoPersistencePort;
import com.aje.salesforce.application.port.out.DetallesDocumentoSalesforcePort;
import com.aje.salesforce.domain.model.DetallesDocumento;
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
@DisplayName("SyncDetallesDocumentoService Tests")
class SyncDetallesDocumentoServiceTest {

    @Mock
    private DetallesDocumentoSalesforcePort detallesDocumentoSalesforcePort;

    @Mock
    private DetallesDocumentoPersistencePort persistencePort;

    @InjectMocks
    private SyncDetallesDocumentoService syncDetallesDocumentoService;

    private DetallesDocumento testDetallesDocumento;

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
    }

    @Test
    @DisplayName("Should sync detalles documento by ID")
    void shouldSyncDetallesDocumentoById() {
        when(detallesDocumentoSalesforcePort.findById("a0X5f000000AbCdEFG")).thenReturn(testDetallesDocumento);
        when(persistencePort.save(any(DetallesDocumento.class))).thenReturn(Mono.just(testDetallesDocumento));

        StepVerifier.create(syncDetallesDocumentoService.syncById("a0X5f000000AbCdEFG"))
                .expectNext(testDetallesDocumento)
                .verifyComplete();

        verify(detallesDocumentoSalesforcePort, times(1)).findById("a0X5f000000AbCdEFG");
        verify(persistencePort, times(1)).save(testDetallesDocumento);
    }

    @Test
    @DisplayName("Should handle error when syncing detalles documento by ID")
    void shouldHandleErrorWhenSyncingDetallesDocumentoById() {
        when(detallesDocumentoSalesforcePort.findById("invalid-id")).thenReturn(testDetallesDocumento);
        when(persistencePort.save(any(DetallesDocumento.class))).thenReturn(Mono.error(new RuntimeException("DB Error")));

        StepVerifier.create(syncDetallesDocumentoService.syncById("invalid-id"))
                .expectError(RuntimeException.class)
                .verify();

        verify(detallesDocumentoSalesforcePort, times(1)).findById("invalid-id");
        verify(persistencePort, times(1)).save(testDetallesDocumento);
    }
}
