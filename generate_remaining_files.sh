#!/bin/bash

# Create CompaniaServiceTest
cat > src/test/java/com/aje/salesforce/application/service/CompaniaServiceTest.java << 'EOF'
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
        
        List<Compania> result = companiaService.execute("Peru");
        
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo("a0X5f000000AbCdEFG");
        verify(salesforcePort, times(1)).findByPais("Peru");
    }
    
    @Test
    @DisplayName("Should get company by ID")
    void shouldGetCompanyById() {
        when(salesforcePort.findById("a0X5f000000AbCdEFG")).thenReturn(testCompania);
        
        Compania result = companiaService.execute("a0X5f000000AbCdEFG");
        
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("a0X5f000000AbCdEFG");
        assertThat(result.getName()).isEqualTo("AJE PERU");
        verify(salesforcePort, times(1)).findById("a0X5f000000AbCdEFG");
    }
    
    @Test
    @DisplayName("Should return empty list when no companies found")
    void shouldReturnEmptyListWhenNoCompaniesFound() {
        when(salesforcePort.findByPais(anyString())).thenReturn(Arrays.asList());
        
        List<Compania> result = companiaService.execute("UnknownCountry");
        
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        verify(salesforcePort, times(1)).findByPais("UnknownCountry");
    }
}
EOF

# Create CompaniaControllerIntegrationTest
cat > src/test/java/com/aje/salesforce/infrastructure/adapter/in/rest/CompaniaControllerIntegrationTest.java << 'EOF'
package com.aje.salesforce.infrastructure.adapter.in.rest;

import com.aje.salesforce.application.port.in.GetCompaniaByIdUseCase;
import com.aje.salesforce.application.port.in.GetCompaniasByPaisUseCase;
import com.aje.salesforce.domain.exception.CompaniaNotFoundException;
import com.aje.salesforce.domain.model.Compania;
import com.aje.salesforce.infrastructure.adapter.in.rest.mapper.CompaniaDtoMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CompaniaController.class)
@ActiveProfiles("test")
@DisplayName("CompaniaController Integration Tests")
class CompaniaControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private GetCompaniasByPaisUseCase getCompaniasByPaisUseCase;
    
    @MockBean
    private GetCompaniaByIdUseCase getCompaniaByIdUseCase;
    
    @MockBean
    private CompaniaDtoMapper mapper;
    
    @Test
    @DisplayName("GET /api/v1/companias should return companies for valid country")
    void shouldReturnCompaniesForValidCountry() throws Exception {
        Compania compania = Compania.builder()
            .id("a0X5f000000AbCdEFG")
            .name("AJE PERU")
            .codigo("PE001")
            .pais("Peru")
            .estado("Activo")
            .isDeleted(false)
            .createdDate(LocalDateTime.now())
            .build();
        
        when(getCompaniasByPaisUseCase.execute("Peru")).thenReturn(Arrays.asList(compania));
        when(mapper.toDtoList(anyList())).thenCallRealMethod();
        
        mockMvc.perform(get("/api/v1/companias")
                .param("pais", "Peru")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
    
    @Test
    @DisplayName("GET /api/v1/companias should return empty list when no companies found")
    void shouldReturnEmptyListWhenNoCompaniesFound() throws Exception {
        when(getCompaniasByPaisUseCase.execute(anyString())).thenReturn(Collections.emptyList());
        
        mockMvc.perform(get("/api/v1/companias")
                .param("pais", "UnknownCountry")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }
    
    @Test
    @DisplayName("GET /api/v1/companias/{id} should return company when found")
    void shouldReturnCompanyWhenFound() throws Exception {
        Compania compania = Compania.builder()
            .id("a0X5f000000AbCdEFG")
            .name("AJE PERU")
            .codigo("PE001")
            .pais("Peru")
            .build();
        
        when(getCompaniaByIdUseCase.execute("a0X5f000000AbCdEFG")).thenReturn(compania);
        
        mockMvc.perform(get("/api/v1/companias/a0X5f000000AbCdEFG")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }
    
    @Test
    @DisplayName("GET /api/v1/companias/{id} should return 404 when company not found")
    void shouldReturn404WhenCompanyNotFound() throws Exception {
        when(getCompaniaByIdUseCase.execute(anyString()))
            .thenThrow(new CompaniaNotFoundException("notfound"));
        
        mockMvc.perform(get("/api/v1/companias/notfound")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }
}
EOF

# Create SalesforceAdapterTest
cat > src/test/java/com/aje/salesforce/infrastructure/adapter/out/salesforce/SalesforceAdapterTest.java << 'EOF'
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
            .createdDate(LocalDateTime.now())
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
EOF

# Create application-test.yml
cat > src/test/resources/application-test.yml << 'EOF'
spring:
  profiles:
    active: test

salesforce:
  login-url: https://test.salesforce.com
  username: test@test.com
  password: testpassword
  client-id: test-client-id
  client-secret: test-client-secret
  timeout: 5

logging:
  level:
    root: INFO
    com.aje.salesforce: DEBUG
EOF

chmod +x generate_remaining_files.sh
