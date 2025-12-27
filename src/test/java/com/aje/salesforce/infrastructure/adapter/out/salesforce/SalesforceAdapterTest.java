package com.aje.salesforce.infrastructure.adapter.out.salesforce;

import com.aje.salesforce.domain.exception.CompaniaNotFoundException;
import com.aje.salesforce.domain.model.Compania;
import com.aje.salesforce.infrastructure.adapter.out.salesforce.client.SalesforceClient;
import com.aje.salesforce.infrastructure.adapter.out.salesforce.mapper.CompaniaMapper;
import com.aje.salesforce.infrastructure.adapter.out.salesforce.response.CompaniaResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SalesforceAdapter Tests")
class SalesforceAdapterTest {
    
    @Mock
    private SalesforceClient salesforceClient;
    
    @Mock
    private CompaniaMapper mapper;
    
    @InjectMocks
    private SalesforceAdapter salesforceAdapter;
    
    private CompaniaResponse testResponse;
    private Compania testCompania;
    
    @BeforeEach
    void setUp() {
        testResponse = CompaniaResponse.builder()
            .id("a0X5f000000AbCdEFG")
            .name("AJE PERU")
            .codigo("PE001")
            .estado("Activo")
            .pais("Peru")
            .isDeleted(false)
            .createdDate(ZonedDateTime.now())
            .build();
        
        testCompania = Compania.builder()
            .id("a0X5f000000AbCdEFG")
            .name("AJE PERU")
            .codigo("PE001")
            .estado("Activo")
            .pais("Peru")
            .isDeleted(false)
            .createdDate(LocalDateTime.now())
            .build();
    }
    
    @Test
    @DisplayName("Should find companies by country")
    void shouldFindCompaniesByCountry() {
        when(salesforceClient.queryByPais("Peru")).thenReturn(Arrays.asList(testResponse));
        when(mapper.toDomainList(anyList())).thenReturn(Arrays.asList(testCompania));
        
        List<Compania> result = salesforceAdapter.findByPais("Peru");
        
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo("a0X5f000000AbCdEFG");
        verify(salesforceClient, times(1)).queryByPais("Peru");
        verify(mapper, times(1)).toDomainList(anyList());
    }
    
    @Test
    @DisplayName("Should find company by ID")
    void shouldFindCompanyById() {
        when(salesforceClient.queryById("a0X5f000000AbCdEFG")).thenReturn(testResponse);
        when(mapper.toDomain(testResponse)).thenReturn(testCompania);
        
        Compania result = salesforceAdapter.findById("a0X5f000000AbCdEFG");
        
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("a0X5f000000AbCdEFG");
        assertThat(result.getName()).isEqualTo("AJE PERU");
        verify(salesforceClient, times(1)).queryById("a0X5f000000AbCdEFG");
        verify(mapper, times(1)).toDomain(testResponse);
    }
    
    @Test
    @DisplayName("Should throw CompaniaNotFoundException when company not found")
    void shouldThrowExceptionWhenCompanyNotFound() {
        when(salesforceClient.queryById(anyString())).thenReturn(null);
        
        assertThatThrownBy(() -> salesforceAdapter.findById("notfound"))
            .isInstanceOf(CompaniaNotFoundException.class)
            .hasMessageContaining("notfound");
        
        verify(salesforceClient, times(1)).queryById("notfound");
        verify(mapper, never()).toDomain(any());
    }
}
