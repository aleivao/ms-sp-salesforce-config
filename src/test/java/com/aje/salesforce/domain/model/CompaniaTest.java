package com.aje.salesforce.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Compania Model Tests")
class CompaniaTest {
    
    @Test
    @DisplayName("Should create Compania with builder")
    void shouldCreateCompaniaWithBuilder() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        
        // When
        Compania compania = Compania.builder()
            .id("a0X5f000000AbCdEFG")
            .name("AJE PERU")
            .ownerId("owner123")
            .isDeleted(false)
            .currencyIsoCode("PEN")
            .createdDate(now)
            .createdById("user123")
            .lastModifiedDate(now)
            .lastModifiedById("user123")
            .systemModstamp(now)
            .lastActivityDate(now)
            .lastViewedDate(now)
            .lastReferencedDate(now)
            .codigo("PE001")
            .estado("Activo")
            .pais("Peru")
            .direccion("Av. Lima 123")
            .telefono("+51987654321")
            .acronimo("AJE")
            .integracionErp("SAP")
            .habilitarSeleccionComprobante(true)
            .habilitarComprobantePreventa(true)
            .habilitarImpuestos(true)
            .impuesto("IGV")
            .habilitarExtraModulo(false)
            .habilitarLineaDeCredito(true)
            .acceso("Full")
            .correoCompania("contacto@aje.com")
            .enviarAOracle(true)
            .enviarASiesa(false)
            .sellin(true)
            .build();
        
        // Then
        assertThat(compania).isNotNull();
        assertThat(compania.getId()).isEqualTo("a0X5f000000AbCdEFG");
        assertThat(compania.getName()).isEqualTo("AJE PERU");
        assertThat(compania.getOwnerId()).isEqualTo("owner123");
        assertThat(compania.getIsDeleted()).isFalse();
        assertThat(compania.getCurrencyIsoCode()).isEqualTo("PEN");
        assertThat(compania.getCreatedDate()).isEqualTo(now);
        assertThat(compania.getCreatedById()).isEqualTo("user123");
        assertThat(compania.getLastModifiedDate()).isEqualTo(now);
        assertThat(compania.getLastModifiedById()).isEqualTo("user123");
        assertThat(compania.getSystemModstamp()).isEqualTo(now);
        assertThat(compania.getLastActivityDate()).isEqualTo(now);
        assertThat(compania.getLastViewedDate()).isEqualTo(now);
        assertThat(compania.getLastReferencedDate()).isEqualTo(now);
        assertThat(compania.getCodigo()).isEqualTo("PE001");
        assertThat(compania.getEstado()).isEqualTo("Activo");
        assertThat(compania.getPais()).isEqualTo("Peru");
        assertThat(compania.getDireccion()).isEqualTo("Av. Lima 123");
        assertThat(compania.getTelefono()).isEqualTo("+51987654321");
        assertThat(compania.getAcronimo()).isEqualTo("AJE");
        assertThat(compania.getIntegracionErp()).isEqualTo("SAP");
        assertThat(compania.getHabilitarSeleccionComprobante()).isTrue();
        assertThat(compania.getHabilitarComprobantePreventa()).isTrue();
        assertThat(compania.getHabilitarImpuestos()).isTrue();
        assertThat(compania.getImpuesto()).isEqualTo("IGV");
        assertThat(compania.getHabilitarExtraModulo()).isFalse();
        assertThat(compania.getHabilitarLineaDeCredito()).isTrue();
        assertThat(compania.getAcceso()).isEqualTo("Full");
        assertThat(compania.getCorreoCompania()).isEqualTo("contacto@aje.com");
        assertThat(compania.getEnviarAOracle()).isTrue();
        assertThat(compania.getEnviarASiesa()).isFalse();
        assertThat(compania.getSellin()).isTrue();
    }
    
    @Test
    @DisplayName("Should use setters correctly")
    void shouldUseSettersCorrectly() {
        // Given
        Compania compania = new Compania();
        LocalDateTime now = LocalDateTime.now();
        
        // When
        compania.setId("a0X5f000000AbCdEFG");
        compania.setName("AJE PERU");
        compania.setOwnerId("owner123");
        compania.setIsDeleted(false);
        compania.setCurrencyIsoCode("PEN");
        compania.setCreatedDate(now);
        compania.setCreatedById("user123");
        compania.setLastModifiedDate(now);
        compania.setLastModifiedById("user123");
        compania.setSystemModstamp(now);
        compania.setLastActivityDate(now);
        compania.setLastViewedDate(now);
        compania.setLastReferencedDate(now);
        compania.setCodigo("PE001");
        compania.setEstado("Activo");
        compania.setPais("Peru");
        compania.setDireccion("Av. Lima 123");
        compania.setTelefono("+51987654321");
        compania.setAcronimo("AJE");
        compania.setIntegracionErp("SAP");
        compania.setHabilitarSeleccionComprobante(true);
        compania.setHabilitarComprobantePreventa(true);
        compania.setHabilitarImpuestos(true);
        compania.setImpuesto("IGV");
        compania.setHabilitarExtraModulo(false);
        compania.setHabilitarLineaDeCredito(true);
        compania.setAcceso("Full");
        compania.setCorreoCompania("contacto@aje.com");
        compania.setEnviarAOracle(true);
        compania.setEnviarASiesa(false);
        compania.setSellin(true);
        
        // Then
        assertThat(compania.getId()).isEqualTo("a0X5f000000AbCdEFG");
        assertThat(compania.getName()).isEqualTo("AJE PERU");
        assertThat(compania.getPais()).isEqualTo("Peru");
    }
    
    @Test
    @DisplayName("Should handle toString correctly")
    void shouldHandleToStringCorrectly() {
        // Given
        Compania compania = Compania.builder()
            .id("123")
            .name("Test Company")
            .pais("Peru")
            .build();
        
        // When
        String result = compania.toString();
        
        // Then
        assertThat(result).contains("123");
        assertThat(result).contains("Test Company");
        assertThat(result).contains("Peru");
    }
    
    @Test
    @DisplayName("Should handle equals and hashCode correctly")
    void shouldHandleEqualsAndHashCodeCorrectly() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        Compania compania1 = Compania.builder()
            .id("123")
            .name("Test")
            .createdDate(now)
            .build();
        
        Compania compania2 = Compania.builder()
            .id("123")
            .name("Test")
            .createdDate(now)
            .build();
        
        Compania compania3 = Compania.builder()
            .id("456")
            .name("Different")
            .createdDate(now)
            .build();
        
        // Then
        assertThat(compania1).isEqualTo(compania2);
        assertThat(compania1).isNotEqualTo(compania3);
        assertThat(compania1.hashCode()).isEqualTo(compania2.hashCode());
    }
    
    @Test
    @DisplayName("Should create empty Compania")
    void shouldCreateEmptyCompania() {
        // When
        Compania compania = new Compania();
        
        // Then
        assertThat(compania).isNotNull();
        assertThat(compania.getId()).isNull();
        assertThat(compania.getName()).isNull();
    }
    
    @Test
    @DisplayName("Should build Compania with minimal fields")
    void shouldBuildCompaniaWithMinimalFields() {
        // When
        Compania compania = Compania.builder()
            .id("123")
            .name("Minimal Company")
            .build();

        // Then
        assertThat(compania).isNotNull();
        assertThat(compania.getId()).isEqualTo("123");
        assertThat(compania.getName()).isEqualTo("Minimal Company");
        assertThat(compania.getPais()).isNull();
    }

    @Test
    @DisplayName("Should return true when isActive with estado Activo and not deleted")
    void shouldReturnTrueWhenIsActiveWithEstadoActivoAndNotDeleted() {
        Compania compania = Compania.builder()
            .isDeleted(false)
            .estado("Activo")
            .build();

        assertThat(compania.isActive()).isTrue();
    }

    @Test
    @DisplayName("Should return true when isActive with estado activo lowercase")
    void shouldReturnTrueWhenIsActiveWithEstadoActivoLowercase() {
        Compania compania = Compania.builder()
            .isDeleted(false)
            .estado("activo")
            .build();

        assertThat(compania.isActive()).isTrue();
    }

    @Test
    @DisplayName("Should return false when isActive but isDeleted is true")
    void shouldReturnFalseWhenIsActiveButIsDeletedIsTrue() {
        Compania compania = Compania.builder()
            .isDeleted(true)
            .estado("Activo")
            .build();

        assertThat(compania.isActive()).isFalse();
    }

    @Test
    @DisplayName("Should return false when isActive but estado is not Activo")
    void shouldReturnFalseWhenIsActiveButEstadoIsNotActivo() {
        Compania compania = Compania.builder()
            .isDeleted(false)
            .estado("Inactivo")
            .build();

        assertThat(compania.isActive()).isFalse();
    }

    @Test
    @DisplayName("Should return false when isActive with null isDeleted")
    void shouldReturnFalseWhenIsActiveWithNullIsDeleted() {
        Compania compania = Compania.builder()
            .isDeleted(null)
            .estado("Activo")
            .build();

        assertThat(compania.isActive()).isFalse();
    }

    @Test
    @DisplayName("Should return true when belongsToCountry matches")
    void shouldReturnTrueWhenBelongsToCountryMatches() {
        Compania compania = Compania.builder()
            .pais("Peru")
            .build();

        assertThat(compania.belongsToCountry("Peru")).isTrue();
    }

    @Test
    @DisplayName("Should return true when belongsToCountry matches case insensitive")
    void shouldReturnTrueWhenBelongsToCountryMatchesCaseInsensitive() {
        Compania compania = Compania.builder()
            .pais("Peru")
            .build();

        assertThat(compania.belongsToCountry("PERU")).isTrue();
        assertThat(compania.belongsToCountry("peru")).isTrue();
    }

    @Test
    @DisplayName("Should return false when belongsToCountry does not match")
    void shouldReturnFalseWhenBelongsToCountryDoesNotMatch() {
        Compania compania = Compania.builder()
            .pais("Peru")
            .build();

        assertThat(compania.belongsToCountry("Colombia")).isFalse();
    }

    @Test
    @DisplayName("Should return false when belongsToCountry with null country parameter")
    void shouldReturnFalseWhenBelongsToCountryWithNullParameter() {
        Compania compania = Compania.builder()
            .pais("Peru")
            .build();

        assertThat(compania.belongsToCountry(null)).isFalse();
    }

    @Test
    @DisplayName("Should return false when belongsToCountry with null pais")
    void shouldReturnFalseWhenBelongsToCountryWithNullPais() {
        Compania compania = Compania.builder()
            .pais(null)
            .build();

        assertThat(compania.belongsToCountry("Peru")).isFalse();
    }
}
