package com.aje.salesforce.application.service;

import com.aje.salesforce.application.port.out.DetallesDocumentoSalesforcePort;
import com.aje.salesforce.domain.exception.DetallesDocumentoNotFoundException;
import com.aje.salesforce.domain.model.DetallesDocumento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DetallesDocumentoService Tests")
class DetallesDocumentoServiceTest {

    @Mock
    private DetallesDocumentoSalesforcePort detallesDocumentoSalesforcePort;

    @InjectMocks
    private DetallesDocumentoService detallesDocumentoService;

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
    @DisplayName("Should return detalles documento when found by ID")
    void shouldReturnDetallesDocumentoWhenFoundById() {
        when(detallesDocumentoSalesforcePort.findById("a0X5f000000AbCdEFG")).thenReturn(testDetallesDocumento);

        DetallesDocumento result = detallesDocumentoService.getById("a0X5f000000AbCdEFG");

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("a0X5f000000AbCdEFG");
        assertThat(result.getName()).isEqualTo("Detalle 001");
        verify(detallesDocumentoSalesforcePort, times(1)).findById("a0X5f000000AbCdEFG");
    }

    @Test
    @DisplayName("Should throw exception when detalles documento not found by ID")
    void shouldThrowExceptionWhenDetallesDocumentoNotFoundById() {
        when(detallesDocumentoSalesforcePort.findById("invalid-id"))
            .thenThrow(new DetallesDocumentoNotFoundException("invalid-id"));

        assertThatThrownBy(() -> detallesDocumentoService.getById("invalid-id"))
            .isInstanceOf(DetallesDocumentoNotFoundException.class)
            .hasMessageContaining("invalid-id");

        verify(detallesDocumentoSalesforcePort, times(1)).findById("invalid-id");
    }

    @Test
    @DisplayName("Should return detalles documento when found by configuracion")
    void shouldReturnDetallesDocumentoWhenFoundByConfiguracion() {
        List<DetallesDocumento> detallesDocumentos = Arrays.asList(testDetallesDocumento);
        when(detallesDocumentoSalesforcePort.findByConfiguracionImpresion("a0X5f000000Config1")).thenReturn(detallesDocumentos);

        List<DetallesDocumento> result = detallesDocumentoService.getByConfiguracion("a0X5f000000Config1");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getConfiguracionDeImpresion()).isEqualTo("a0X5f000000Config1");
        verify(detallesDocumentoSalesforcePort, times(1)).findByConfiguracionImpresion("a0X5f000000Config1");
    }

    @Test
    @DisplayName("Should return empty list when no detalles documento found by configuracion")
    void shouldReturnEmptyListWhenNoDetallesDocumentoFoundByConfiguracion() {
        when(detallesDocumentoSalesforcePort.findByConfiguracionImpresion("invalid-config")).thenReturn(Collections.emptyList());

        List<DetallesDocumento> result = detallesDocumentoService.getByConfiguracion("invalid-config");

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        verify(detallesDocumentoSalesforcePort, times(1)).findByConfiguracionImpresion("invalid-config");
    }
}
