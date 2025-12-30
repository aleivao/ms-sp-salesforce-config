package com.aje.salesforce.infrastructure.adapter.out.salesforce;

import com.aje.salesforce.domain.exception.ConfiguracionImpresionNotFoundException;
import com.aje.salesforce.domain.model.ConfiguracionImpresion;
import com.aje.salesforce.infrastructure.adapter.out.salesforce.client.SalesforceClient;
import com.aje.salesforce.infrastructure.adapter.out.salesforce.mapper.ConfiguracionImpresionMapper;
import com.aje.salesforce.infrastructure.adapter.out.salesforce.response.ConfiguracionImpresionResponse;
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
@DisplayName("ConfiguracionImpresionSalesforceAdapter Tests")
class ConfiguracionImpresionSalesforceAdapterTest {

    @Mock
    private SalesforceClient salesforceClient;

    @Mock
    private ConfiguracionImpresionMapper mapper;

    @InjectMocks
    private ConfiguracionImpresionSalesforceAdapter adapter;

    private ConfiguracionImpresionResponse testResponse;
    private ConfiguracionImpresion testConfiguracion;

    @BeforeEach
    void setUp() {
        testResponse = ConfiguracionImpresionResponse.builder()
                .id("a0X5f000000AbCdEFG")
                .name("Config Factura PE")
                .pais("PE")
                .tipoDocumento("Factura")
                .documentoDefault(true)
                .isDeleted(false)
                .build();

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
    @DisplayName("Should return configuracion when found by ID")
    void shouldReturnConfiguracionWhenFoundById() {
        when(salesforceClient.queryConfiguracionImpresionById("a0X5f000000AbCdEFG")).thenReturn(testResponse);
        when(mapper.toDomain(testResponse)).thenReturn(testConfiguracion);

        ConfiguracionImpresion result = adapter.findById("a0X5f000000AbCdEFG");

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("a0X5f000000AbCdEFG");
        assertThat(result.getName()).isEqualTo("Config Factura PE");
        verify(salesforceClient, times(1)).queryConfiguracionImpresionById("a0X5f000000AbCdEFG");
        verify(mapper, times(1)).toDomain(testResponse);
    }

    @Test
    @DisplayName("Should throw exception when configuracion not found by ID")
    void shouldThrowExceptionWhenConfiguracionNotFoundById() {
        when(salesforceClient.queryConfiguracionImpresionById("invalid-id")).thenReturn(null);

        assertThatThrownBy(() -> adapter.findById("invalid-id"))
            .isInstanceOf(ConfiguracionImpresionNotFoundException.class)
            .hasMessageContaining("invalid-id");

        verify(salesforceClient, times(1)).queryConfiguracionImpresionById("invalid-id");
        verify(mapper, never()).toDomain(any());
    }

    @Test
    @DisplayName("Should return configuraciones when found by country")
    void shouldReturnConfiguracionesWhenFoundByCountry() {
        List<ConfiguracionImpresionResponse> responses = Arrays.asList(testResponse);
        List<ConfiguracionImpresion> configuraciones = Arrays.asList(testConfiguracion);

        when(salesforceClient.queryConfiguracionImpresionByPais("PE")).thenReturn(responses);
        when(mapper.toDomainList(responses)).thenReturn(configuraciones);

        List<ConfiguracionImpresion> result = adapter.findByPais("PE");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getPais()).isEqualTo("PE");
        verify(salesforceClient, times(1)).queryConfiguracionImpresionByPais("PE");
        verify(mapper, times(1)).toDomainList(responses);
    }

    @Test
    @DisplayName("Should return empty list when no configuraciones found by country")
    void shouldReturnEmptyListWhenNoConfiguracionesFoundByCountry() {
        when(salesforceClient.queryConfiguracionImpresionByPais("XX")).thenReturn(Collections.emptyList());
        when(mapper.toDomainList(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<ConfiguracionImpresion> result = adapter.findByPais("XX");

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        verify(salesforceClient, times(1)).queryConfiguracionImpresionByPais("XX");
    }
}
