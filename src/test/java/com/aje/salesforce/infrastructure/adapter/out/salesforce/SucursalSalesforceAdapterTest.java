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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
                .pais("PE")
                .isDeleted(false)
                .build();

        testSucursal = Sucursal.builder()
                .id("a0Y5f000000AbCdEFG")
                .name("Sucursal Lima Norte")
                .codigo("SUC001")
                .estado("Activo")
                .pais("PE")
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

    @Test
    @DisplayName("Should return sucursales when found by country")
    void shouldReturnSucursalesWhenFoundByCountry() {
        List<SucursalResponse> responses = Arrays.asList(testResponse);
        List<Sucursal> sucursales = Arrays.asList(testSucursal);

        when(salesforceClient.querySucursalByPais("PE")).thenReturn(responses);
        when(mapper.toDomainList(responses)).thenReturn(sucursales);

        List<Sucursal> result = adapter.findByPais("PE");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getPais()).isEqualTo("PE");
        verify(salesforceClient, times(1)).querySucursalByPais("PE");
        verify(mapper, times(1)).toDomainList(responses);
    }

    @Test
    @DisplayName("Should return empty list when no sucursales found by country")
    void shouldReturnEmptyListWhenNoSucursalesFoundByCountry() {
        when(salesforceClient.querySucursalByPais("XX")).thenReturn(Collections.emptyList());
        when(mapper.toDomainList(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<Sucursal> result = adapter.findByPais("XX");

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        verify(salesforceClient, times(1)).querySucursalByPais("XX");
    }
}
