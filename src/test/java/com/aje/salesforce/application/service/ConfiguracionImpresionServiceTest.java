package com.aje.salesforce.application.service;

import com.aje.salesforce.application.port.out.ConfiguracionImpresionSalesforcePort;
import com.aje.salesforce.domain.exception.ConfiguracionImpresionNotFoundException;
import com.aje.salesforce.domain.model.ConfiguracionImpresion;
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
@DisplayName("ConfiguracionImpresionService Tests")
class ConfiguracionImpresionServiceTest {

    @Mock
    private ConfiguracionImpresionSalesforcePort configuracionImpresionSalesforcePort;

    @InjectMocks
    private ConfiguracionImpresionService configuracionImpresionService;

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
    @DisplayName("Should return configuracion when found by ID")
    void shouldReturnConfiguracionWhenFoundById() {
        when(configuracionImpresionSalesforcePort.findById("a0X5f000000AbCdEFG")).thenReturn(testConfiguracion);

        ConfiguracionImpresion result = configuracionImpresionService.getById("a0X5f000000AbCdEFG");

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("a0X5f000000AbCdEFG");
        assertThat(result.getName()).isEqualTo("Config Factura PE");
        verify(configuracionImpresionSalesforcePort, times(1)).findById("a0X5f000000AbCdEFG");
    }

    @Test
    @DisplayName("Should throw exception when configuracion not found by ID")
    void shouldThrowExceptionWhenConfiguracionNotFoundById() {
        when(configuracionImpresionSalesforcePort.findById("invalid-id"))
            .thenThrow(new ConfiguracionImpresionNotFoundException("invalid-id"));

        assertThatThrownBy(() -> configuracionImpresionService.getById("invalid-id"))
            .isInstanceOf(ConfiguracionImpresionNotFoundException.class)
            .hasMessageContaining("invalid-id");

        verify(configuracionImpresionSalesforcePort, times(1)).findById("invalid-id");
    }

    @Test
    @DisplayName("Should return configuraciones when found by country")
    void shouldReturnConfiguracionesWhenFoundByCountry() {
        List<ConfiguracionImpresion> configuraciones = Arrays.asList(testConfiguracion);
        when(configuracionImpresionSalesforcePort.findByPais("PE")).thenReturn(configuraciones);

        List<ConfiguracionImpresion> result = configuracionImpresionService.getByPais("PE");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getPais()).isEqualTo("PE");
        verify(configuracionImpresionSalesforcePort, times(1)).findByPais("PE");
    }

    @Test
    @DisplayName("Should return empty list when no configuraciones found by country")
    void shouldReturnEmptyListWhenNoConfiguracionesFoundByCountry() {
        when(configuracionImpresionSalesforcePort.findByPais("XX")).thenReturn(Collections.emptyList());

        List<ConfiguracionImpresion> result = configuracionImpresionService.getByPais("XX");

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        verify(configuracionImpresionSalesforcePort, times(1)).findByPais("XX");
    }
}
