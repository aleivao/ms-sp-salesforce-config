package com.aje.salesforce.infrastructure.adapter.in.rest;

import com.aje.salesforce.application.port.in.GetCompaniaByIdUseCase;
import com.aje.salesforce.application.port.in.GetCompaniasByPaisUseCase;
import com.aje.salesforce.application.port.in.SyncCompaniasUseCase;
import com.aje.salesforce.domain.exception.CompaniaNotFoundException;
import com.aje.salesforce.domain.model.Compania;
import com.aje.salesforce.infrastructure.adapter.in.rest.dto.CompaniaDto;
import com.aje.salesforce.infrastructure.adapter.in.rest.mapper.CompaniaDtoMapper;
import com.aje.salesforce.infrastructure.config.ApiKeyProperties;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CompaniaController.class)
@ActiveProfiles("test")
@DisplayName("CompaniaController Integration Tests")
class CompaniaControllerIntegrationTest {

    private static final String API_KEY = "test-api-key";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetCompaniasByPaisUseCase getCompaniasByPaisUseCase;

    @MockBean
    private GetCompaniaByIdUseCase getCompaniaByIdUseCase;

    @MockBean
    private SyncCompaniasUseCase syncCompaniasUseCase;

    @MockBean
    private CompaniaDtoMapper mapper;

    @MockBean
    private ApiKeyProperties apiKeyProperties;

    @BeforeEach
    void setUp() {
        when(apiKeyProperties.getHeaderName()).thenReturn("X-API-Key");
        when(apiKeyProperties.getValue()).thenReturn(API_KEY);
    }

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
        
        when(getCompaniasByPaisUseCase.getByPais("Peru")).thenReturn(Arrays.asList(compania));
        when(mapper.toDtoList(anyList())).thenReturn(Collections.emptyList());
        
        mockMvc.perform(get("/api/v1/companias")
                .header("X-API-Key", API_KEY)
                .param("pais", "Peru")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("GET /api/v1/companias should return empty list when no companies found")
    void shouldReturnEmptyListWhenNoCompaniesFound() throws Exception {
        when(getCompaniasByPaisUseCase.getByPais(anyString())).thenReturn(Collections.emptyList());
        when(mapper.toDtoList(anyList())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/companias")
                .header("X-API-Key", API_KEY)
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

        CompaniaDto dto = new CompaniaDto();
        dto.setId("a0X5f000000AbCdEFG");
        dto.setName("AJE PERU");

        when(getCompaniaByIdUseCase.getById("a0X5f000000AbCdEFG")).thenReturn(compania);
        when(mapper.toDto(compania)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/companias/a0X5f000000AbCdEFG")
                .header("X-API-Key", API_KEY)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/v1/companias/{id} should return 404 when company not found")
    void shouldReturn404WhenCompanyNotFound() throws Exception {
        when(getCompaniaByIdUseCase.getById(anyString()))
            .thenThrow(new CompaniaNotFoundException("notfound"));

        mockMvc.perform(get("/api/v1/companias/notfound")
                .header("X-API-Key", API_KEY)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }
}
