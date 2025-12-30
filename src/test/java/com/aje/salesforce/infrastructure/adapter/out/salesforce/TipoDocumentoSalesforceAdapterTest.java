package com.aje.salesforce.infrastructure.adapter.out.salesforce;

import com.aje.salesforce.domain.exception.TipoDocumentoNotFoundException;
import com.aje.salesforce.domain.model.TipoDocumento;
import com.aje.salesforce.infrastructure.adapter.out.salesforce.client.SalesforceClient;
import com.aje.salesforce.infrastructure.adapter.out.salesforce.mapper.TipoDocumentoMapper;
import com.aje.salesforce.infrastructure.adapter.out.salesforce.response.TipoDocumentoResponse;
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
@DisplayName("TipoDocumentoSalesforceAdapter Tests")
class TipoDocumentoSalesforceAdapterTest {

    @Mock
    private SalesforceClient salesforceClient;

    @Mock
    private TipoDocumentoMapper mapper;

    @InjectMocks
    private TipoDocumentoSalesforceAdapter adapter;

    private TipoDocumentoResponse testResponse;
    private TipoDocumento testTipoDocumento;

    @BeforeEach
    void setUp() {
        testResponse = TipoDocumentoResponse.builder()
                .id("a0X5f000000AbCdEFG")
                .name("DNI")
                .pais("PE")
                .codigo("DNI")
                .activo(true)
                .isDeleted(false)
                .longitud(8.0)
                .build();

        testTipoDocumento = TipoDocumento.builder()
                .id("a0X5f000000AbCdEFG")
                .name("DNI")
                .pais("PE")
                .codigo("DNI")
                .activo(true)
                .isDeleted(false)
                .longitud(8.0)
                .createdDate(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Should return tipo documento when found by ID")
    void shouldReturnTipoDocumentoWhenFoundById() {
        when(salesforceClient.queryTipoDocumentoById("a0X5f000000AbCdEFG")).thenReturn(testResponse);
        when(mapper.toDomain(testResponse)).thenReturn(testTipoDocumento);

        TipoDocumento result = adapter.findById("a0X5f000000AbCdEFG");

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("a0X5f000000AbCdEFG");
        assertThat(result.getName()).isEqualTo("DNI");
        verify(salesforceClient, times(1)).queryTipoDocumentoById("a0X5f000000AbCdEFG");
        verify(mapper, times(1)).toDomain(testResponse);
    }

    @Test
    @DisplayName("Should throw exception when tipo documento not found by ID")
    void shouldThrowExceptionWhenTipoDocumentoNotFoundById() {
        when(salesforceClient.queryTipoDocumentoById("invalid-id")).thenReturn(null);

        assertThatThrownBy(() -> adapter.findById("invalid-id"))
            .isInstanceOf(TipoDocumentoNotFoundException.class)
            .hasMessageContaining("invalid-id");

        verify(salesforceClient, times(1)).queryTipoDocumentoById("invalid-id");
        verify(mapper, never()).toDomain(any());
    }

    @Test
    @DisplayName("Should return tipos documento when found by country")
    void shouldReturnTiposDocumentoWhenFoundByCountry() {
        List<TipoDocumentoResponse> responses = Arrays.asList(testResponse);
        List<TipoDocumento> tiposDocumento = Arrays.asList(testTipoDocumento);

        when(salesforceClient.queryTipoDocumentoByPais("PE")).thenReturn(responses);
        when(mapper.toDomainList(responses)).thenReturn(tiposDocumento);

        List<TipoDocumento> result = adapter.findByPais("PE");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getPais()).isEqualTo("PE");
        verify(salesforceClient, times(1)).queryTipoDocumentoByPais("PE");
        verify(mapper, times(1)).toDomainList(responses);
    }

    @Test
    @DisplayName("Should return empty list when no tipos documento found by country")
    void shouldReturnEmptyListWhenNoTiposDocumentoFoundByCountry() {
        when(salesforceClient.queryTipoDocumentoByPais("XX")).thenReturn(Collections.emptyList());
        when(mapper.toDomainList(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<TipoDocumento> result = adapter.findByPais("XX");

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        verify(salesforceClient, times(1)).queryTipoDocumentoByPais("XX");
    }
}
