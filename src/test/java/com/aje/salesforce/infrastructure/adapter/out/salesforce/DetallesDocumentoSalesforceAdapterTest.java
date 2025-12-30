package com.aje.salesforce.infrastructure.adapter.out.salesforce;

import com.aje.salesforce.domain.exception.DetallesDocumentoNotFoundException;
import com.aje.salesforce.domain.model.DetallesDocumento;
import com.aje.salesforce.infrastructure.adapter.out.salesforce.client.SalesforceClient;
import com.aje.salesforce.infrastructure.adapter.out.salesforce.mapper.DetallesDocumentoMapper;
import com.aje.salesforce.infrastructure.adapter.out.salesforce.response.DetallesDocumentoResponse;
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
@DisplayName("DetallesDocumentoSalesforceAdapter Tests")
class DetallesDocumentoSalesforceAdapterTest {

    @Mock
    private SalesforceClient salesforceClient;

    @Mock
    private DetallesDocumentoMapper mapper;

    @InjectMocks
    private DetallesDocumentoSalesforceAdapter adapter;

    private DetallesDocumentoResponse testResponse;
    private DetallesDocumento testDetallesDocumento;

    @BeforeEach
    void setUp() {
        testResponse = DetallesDocumentoResponse.builder()
                .id("a0X5f000000AbCdEFG")
                .name("Detalle 001")
                .configuracionDeImpresion("a0X5f000000Config1")
                .documentoDeVenta("Factura")
                .texto1("Texto de prueba")
                .isDeleted(false)
                .build();

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
        when(salesforceClient.queryDetallesDocumentoById("a0X5f000000AbCdEFG")).thenReturn(testResponse);
        when(mapper.toDomain(testResponse)).thenReturn(testDetallesDocumento);

        DetallesDocumento result = adapter.findById("a0X5f000000AbCdEFG");

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("a0X5f000000AbCdEFG");
        assertThat(result.getName()).isEqualTo("Detalle 001");
        verify(salesforceClient, times(1)).queryDetallesDocumentoById("a0X5f000000AbCdEFG");
        verify(mapper, times(1)).toDomain(testResponse);
    }

    @Test
    @DisplayName("Should throw exception when detalles documento not found by ID")
    void shouldThrowExceptionWhenDetallesDocumentoNotFoundById() {
        when(salesforceClient.queryDetallesDocumentoById("invalid-id")).thenReturn(null);

        assertThatThrownBy(() -> adapter.findById("invalid-id"))
            .isInstanceOf(DetallesDocumentoNotFoundException.class)
            .hasMessageContaining("invalid-id");

        verify(salesforceClient, times(1)).queryDetallesDocumentoById("invalid-id");
        verify(mapper, never()).toDomain(any());
    }

    @Test
    @DisplayName("Should return detalles documento when found by configuracion")
    void shouldReturnDetallesDocumentoWhenFoundByConfiguracion() {
        List<DetallesDocumentoResponse> responses = Arrays.asList(testResponse);
        List<DetallesDocumento> detallesDocumentos = Arrays.asList(testDetallesDocumento);

        when(salesforceClient.queryDetallesDocumentoByConfiguracion("a0X5f000000Config1")).thenReturn(responses);
        when(mapper.toDomainList(responses)).thenReturn(detallesDocumentos);

        List<DetallesDocumento> result = adapter.findByConfiguracionImpresion("a0X5f000000Config1");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getConfiguracionDeImpresion()).isEqualTo("a0X5f000000Config1");
        verify(salesforceClient, times(1)).queryDetallesDocumentoByConfiguracion("a0X5f000000Config1");
        verify(mapper, times(1)).toDomainList(responses);
    }

    @Test
    @DisplayName("Should return empty list when no detalles documento found by configuracion")
    void shouldReturnEmptyListWhenNoDetallesDocumentoFoundByConfiguracion() {
        when(salesforceClient.queryDetallesDocumentoByConfiguracion("invalid-config")).thenReturn(Collections.emptyList());
        when(mapper.toDomainList(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<DetallesDocumento> result = adapter.findByConfiguracionImpresion("invalid-config");

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        verify(salesforceClient, times(1)).queryDetallesDocumentoByConfiguracion("invalid-config");
    }
}
