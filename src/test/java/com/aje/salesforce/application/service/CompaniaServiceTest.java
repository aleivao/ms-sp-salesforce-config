package com.aje.salesforce.application.service;

import com.aje.salesforce.application.port.out.SalesforcePort;
import com.aje.salesforce.domain.model.Compania;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CompaniaService Tests")
class CompaniaServiceTest {
    
    @Mock
    private SalesforcePort salesforcePort;
    
    @InjectMocks
    private CompaniaService companiaService;
    
    private Compania testCompania;
    
    @BeforeEach
    void setUp() {
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
    @DisplayName("Should get companies by country")
    void shouldGetCompaniesByCountry() {
        List<Compania> expectedCompanias = Arrays.asList(testCompania);
        when(salesforcePort.findByPais("Peru")).thenReturn(expectedCompanias);
        
        List<Compania> result = companiaService.getByPais("Peru");
        
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo("a0X5f000000AbCdEFG");
        verify(salesforcePort, times(1)).findByPais("Peru");
    }
    
    @Test
    @DisplayName("Should get company by ID")
    void shouldGetCompanyById() {
        when(salesforcePort.findById("a0X5f000000AbCdEFG")).thenReturn(testCompania);
        
        Compania result = companiaService.getById("a0X5f000000AbCdEFG");
        
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("a0X5f000000AbCdEFG");
        assertThat(result.getName()).isEqualTo("AJE PERU");
        verify(salesforcePort, times(1)).findById("a0X5f000000AbCdEFG");
    }
    
    @Test
    @DisplayName("Should return empty list when no companies found")
    void shouldReturnEmptyListWhenNoCompaniesFound() {
        when(salesforcePort.findByPais(anyString())).thenReturn(Arrays.asList());
        
        List<Compania> result = companiaService.getByPais("UnknownCountry");
        
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        verify(salesforcePort, times(1)).findByPais("UnknownCountry");
    }
}
