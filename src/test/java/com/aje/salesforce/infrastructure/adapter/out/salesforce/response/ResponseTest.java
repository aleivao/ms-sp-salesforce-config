package com.aje.salesforce.infrastructure.adapter.out.salesforce.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Response Classes Tests")
class ResponseTest {
    
    @Test
    @DisplayName("Should create SalesforceAuthResponse")
    void shouldCreateSalesforceAuthResponse() {
        SalesforceAuthResponse response = SalesforceAuthResponse.builder()
            .accessToken("token123")
            .instanceUrl("https://instance.com")
            .id("id123")
            .tokenType("Bearer")
            .issuedAt("2024-01-01")
            .signature("sig123")
            .build();
        
        assertThat(response.getAccessToken()).isEqualTo("token123");
        assertThat(response.getInstanceUrl()).isEqualTo("https://instance.com");
        assertThat(response.getId()).isEqualTo("id123");
        assertThat(response.getTokenType()).isEqualTo("Bearer");
        assertThat(response.getIssuedAt()).isEqualTo("2024-01-01");
        assertThat(response.getSignature()).isEqualTo("sig123");
    }
    
    @Test
    @DisplayName("Should create SalesforceAuthResponse with setters")
    void shouldCreateSalesforceAuthResponseWithSetters() {
        SalesforceAuthResponse response = new SalesforceAuthResponse();
        response.setAccessToken("token456");
        response.setInstanceUrl("https://test.com");
        response.setId("id456");
        response.setTokenType("Bearer");
        response.setIssuedAt("2024-01-02");
        response.setSignature("sig456");
        
        assertThat(response.getAccessToken()).isEqualTo("token456");
        assertThat(response.getInstanceUrl()).isEqualTo("https://test.com");
    }
    
    @Test
    @DisplayName("Should create SalesforceQueryResponse")
    void shouldCreateSalesforceQueryResponse() {
        CompaniaResponse compania = CompaniaResponse.builder()
            .id("123")
            .name("Test")
            .build();
        
        SalesforceQueryResponse response = SalesforceQueryResponse.builder()
            .totalSize(1)
            .done(true)
            .records(List.of(compania))
            .build();
        
        assertThat(response.getTotalSize()).isEqualTo(1);
        assertThat(response.getDone()).isTrue();
        assertThat(response.getRecords()).hasSize(1);
    }
    
    @Test
    @DisplayName("Should create CompaniaResponse with all fields")
    void shouldCreateCompaniaResponseWithAllFields() {
        ZonedDateTime now = ZonedDateTime.now();
        
        CompaniaResponse response = CompaniaResponse.builder()
            .id("123")
            .name("Company")
            .ownerId("owner1")
            .isDeleted(false)
            .currencyIsoCode("USD")
            .createdDate(now)
            .createdById("user1")
            .lastModifiedDate(now)
            .lastModifiedById("user2")
            .systemModstamp(now)
            .lastActivityDate(now)
            .lastViewedDate(now)
            .lastReferencedDate(now)
            .codigo("C001")
            .estado("Active")
            .pais("Peru")
            .direccion("Address")
            .telefono("123456")
            .acronimo("ABC")
            .integracionErp("SAP")
            .habilitarSeleccionComprobante(true)
            .habilitarComprobantePreventa(false)
            .habilitarImpuestos(true)
            .impuesto("IGV")
            .habilitarExtraModulo(false)
            .habilitarLineaDeCredito(true)
            .acceso("Full")
            .correoCompania("test@test.com")
            .enviarAOracle(true)
            .enviarASiesa(false)
            .sellin(true)
            .build();
        
        assertThat(response.getId()).isEqualTo("123");
        assertThat(response.getName()).isEqualTo("Company");
        assertThat(response.getPais()).isEqualTo("Peru");
        assertThat(response.getCodigo()).isEqualTo("C001");
        assertThat(response.getHabilitarImpuestos()).isTrue();
    }
    
    @Test
    @DisplayName("Should use setters on CompaniaResponse")
    void shouldUseSettersOnCompaniaResponse() {
        CompaniaResponse response = new CompaniaResponse();
        ZonedDateTime now = ZonedDateTime.now();
        
        response.setId("456");
        response.setName("Test Company");
        response.setPais("Colombia");
        response.setCodigo("C002");
        response.setCreatedDate(now);
        
        assertThat(response.getId()).isEqualTo("456");
        assertThat(response.getName()).isEqualTo("Test Company");
        assertThat(response.getPais()).isEqualTo("Colombia");
    }
}
