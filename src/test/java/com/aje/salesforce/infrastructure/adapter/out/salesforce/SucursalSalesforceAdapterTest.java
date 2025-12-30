package com.aje.salesforce.infrastructure.adapter.out.salesforce;

import com.aje.salesforce.domain.exception.SucursalNotFoundException;
import com.aje.salesforce.domain.model.Sucursal;
import com.aje.salesforce.infrastructure.adapter.out.salesforce.client.SalesforceClient;
import com.aje.salesforce.infrastructure.adapter.out.salesforce.mapper.SucursalMapper;
import com.aje.salesforce.infrastructure.adapter.out.salesforce.response.SucursalResponse;
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
@DisplayName("SucursalSalesforceAdapter Tests")
class SucursalSalesforceAdapterTest {

    @Mock
    private SalesforceClient salesforceClient;

    @Mock
    private SucursalMapper mapper;

    @InjectMocks
    private SucursalSalesforceAdapter adapter;

    private SucursalResponse testResponse;
    private Sucursal testSucursal;

    @BeforeEach
    void setUp() {
        testResponse = SucursalResponse.builder()
                .id("a0Y5f000000AbCdEFG")
                .name("Sucursal Lima Norte")
                .codigo("SUC001")
                .estado("Activo")
                .pais("Peru")
                .isDeleted(false)
                .build();

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
        when(salesforceClient.querySucursalById("a0Y5f000000AbCdEFG")).thenReturn(testResponse);
        when(mapper.toDomain(testResponse)).thenReturn(testSucursal);

        Sucursal result = adapter.findById("a0Y5f000000AbCdEFG");

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("a0Y5f000000AbCdEFG");
        assertThat(result.getName()).isEqualTo("Sucursal Lima Norte");
        verify(salesforceClient, times(1)).querySucursalById("a0Y5f000000AbCdEFG");
        verify(mapper, times(1)).toDomain(testResponse);
    }

    @Test
    @DisplayName("Should throw exception when sucursal not found by ID")
    void shouldThrowExceptionWhenSucursalNotFoundById() {
        when(salesforceClient.querySucursalById("invalid-id")).thenReturn(null);

        assertThatThrownBy(() -> adapter.findById("invalid-id"))
            .isInstanceOf(SucursalNotFoundException.class)
            .hasMessageContaining("invalid-id");

        verify(salesforceClient, times(1)).querySucursalById("invalid-id");
        verify(mapper, never()).toDomain(any());
    }
}
