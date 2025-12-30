package com.aje.salesforce.application.service;

import com.aje.salesforce.application.port.out.SucursalSalesforcePort;
import com.aje.salesforce.domain.exception.SucursalNotFoundException;
import com.aje.salesforce.domain.model.Sucursal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SucursalService Tests")
class SucursalServiceTest {

    @Mock
    private SucursalSalesforcePort sucursalSalesforcePort;

    @InjectMocks
    private SucursalService sucursalService;

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
    @DisplayName("Should return sucursal when found by ID")
    void shouldReturnSucursalWhenFoundById() {
        when(sucursalSalesforcePort.findById("a0Y5f000000AbCdEFG")).thenReturn(testSucursal);

        Sucursal result = sucursalService.getById("a0Y5f000000AbCdEFG");

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("a0Y5f000000AbCdEFG");
        assertThat(result.getName()).isEqualTo("Sucursal Lima Norte");
        verify(sucursalSalesforcePort, times(1)).findById("a0Y5f000000AbCdEFG");
    }

    @Test
    @DisplayName("Should throw exception when sucursal not found by ID")
    void shouldThrowExceptionWhenSucursalNotFoundById() {
        when(sucursalSalesforcePort.findById("invalid-id"))
            .thenThrow(new SucursalNotFoundException("invalid-id"));

        assertThatThrownBy(() -> sucursalService.getById("invalid-id"))
            .isInstanceOf(SucursalNotFoundException.class)
            .hasMessageContaining("invalid-id");

        verify(sucursalSalesforcePort, times(1)).findById("invalid-id");
    }
}
