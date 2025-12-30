package com.aje.salesforce.application.service;

import com.aje.salesforce.application.port.out.TipoDocumentoSalesforcePort;
import com.aje.salesforce.domain.exception.TipoDocumentoNotFoundException;
import com.aje.salesforce.domain.model.TipoDocumento;
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
@DisplayName("TipoDocumentoService Tests")
class TipoDocumentoServiceTest {

    @Mock
    private TipoDocumentoSalesforcePort tipoDocumentoSalesforcePort;

    @InjectMocks
    private TipoDocumentoService tipoDocumentoService;

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
    @DisplayName("Should return tipo documento when found by ID")
    void shouldReturnTipoDocumentoWhenFoundById() {
        when(tipoDocumentoSalesforcePort.findById("a0X5f000000AbCdEFG")).thenReturn(testTipoDocumento);

        TipoDocumento result = tipoDocumentoService.getById("a0X5f000000AbCdEFG");

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("a0X5f000000AbCdEFG");
        assertThat(result.getName()).isEqualTo("DNI");
        verify(tipoDocumentoSalesforcePort, times(1)).findById("a0X5f000000AbCdEFG");
    }

    @Test
    @DisplayName("Should throw exception when tipo documento not found by ID")
    void shouldThrowExceptionWhenTipoDocumentoNotFoundById() {
        when(tipoDocumentoSalesforcePort.findById("invalid-id"))
            .thenThrow(new TipoDocumentoNotFoundException("invalid-id"));

        assertThatThrownBy(() -> tipoDocumentoService.getById("invalid-id"))
            .isInstanceOf(TipoDocumentoNotFoundException.class)
            .hasMessageContaining("invalid-id");

        verify(tipoDocumentoSalesforcePort, times(1)).findById("invalid-id");
    }

    @Test
    @DisplayName("Should return tipos documento when found by country")
    void shouldReturnTiposDocumentoWhenFoundByCountry() {
        List<TipoDocumento> tiposDocumento = Arrays.asList(testTipoDocumento);
        when(tipoDocumentoSalesforcePort.findByPais("PE")).thenReturn(tiposDocumento);

        List<TipoDocumento> result = tipoDocumentoService.getByPais("PE");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getPais()).isEqualTo("PE");
        verify(tipoDocumentoSalesforcePort, times(1)).findByPais("PE");
    }

    @Test
    @DisplayName("Should return empty list when no tipos documento found by country")
    void shouldReturnEmptyListWhenNoTiposDocumentoFoundByCountry() {
        when(tipoDocumentoSalesforcePort.findByPais("XX")).thenReturn(Collections.emptyList());

        List<TipoDocumento> result = tipoDocumentoService.getByPais("XX");

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        verify(tipoDocumentoSalesforcePort, times(1)).findByPais("XX");
    }
}
