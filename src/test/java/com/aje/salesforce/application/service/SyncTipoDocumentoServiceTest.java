package com.aje.salesforce.application.service;

import com.aje.salesforce.application.port.out.TipoDocumentoPersistencePort;
import com.aje.salesforce.application.port.out.TipoDocumentoSalesforcePort;
import com.aje.salesforce.domain.model.TipoDocumento;
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
@DisplayName("SyncTipoDocumentoService Tests")
class SyncTipoDocumentoServiceTest {

    @Mock
    private TipoDocumentoSalesforcePort tipoDocumentoSalesforcePort;

    @Mock
    private TipoDocumentoPersistencePort persistencePort;

    @InjectMocks
    private SyncTipoDocumentoService syncTipoDocumentoService;

    private TipoDocumento testTipoDocumento;

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
                .expresionRegular("^[0-9]{8}$")
                .createdDate(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Should sync tipo documento by ID")
    void shouldSyncTipoDocumentoById() {
        when(tipoDocumentoSalesforcePort.findById("a0X5f000000AbCdEFG")).thenReturn(testTipoDocumento);
        when(persistencePort.save(any(TipoDocumento.class))).thenReturn(Mono.just(testTipoDocumento));

        StepVerifier.create(syncTipoDocumentoService.syncById("a0X5f000000AbCdEFG"))
                .expectNext(testTipoDocumento)
                .verifyComplete();

        verify(tipoDocumentoSalesforcePort, times(1)).findById("a0X5f000000AbCdEFG");
        verify(persistencePort, times(1)).save(testTipoDocumento);
    }

    @Test
    @DisplayName("Should handle error when syncing tipo documento by ID")
    void shouldHandleErrorWhenSyncingTipoDocumentoById() {
        when(tipoDocumentoSalesforcePort.findById("invalid-id")).thenReturn(testTipoDocumento);
        when(persistencePort.save(any(TipoDocumento.class))).thenReturn(Mono.error(new RuntimeException("DB Error")));

        StepVerifier.create(syncTipoDocumentoService.syncById("invalid-id"))
                .expectError(RuntimeException.class)
                .verify();

        verify(tipoDocumentoSalesforcePort, times(1)).findById("invalid-id");
        verify(persistencePort, times(1)).save(testTipoDocumento);
    }
}
