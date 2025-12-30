package com.aje.salesforce.infrastructure.adapter.out.salesforce.client;

import com.aje.salesforce.domain.exception.SalesforceIntegrationException;
import com.aje.salesforce.infrastructure.adapter.out.salesforce.response.CompaniaResponse;
import com.aje.salesforce.infrastructure.adapter.out.salesforce.response.SucursalResponse;
import com.aje.salesforce.infrastructure.config.SalesforceProperties;
import com.fasterxml.jackson.databind.JsonNode;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SalesforceClient {
    
    private final WebClient salesforceWebClient;
    private final SalesforceProperties properties;
    
    private String accessToken;
    private String instanceUrl;
    
    @CircuitBreaker(name = "salesforce", fallbackMethod = "fallbackAuthenticate")
    @Retry(name = "salesforce")
    public void authenticate() {
        log.info("Authenticating with Salesforce...");
        
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "password");
        formData.add("client_id", properties.getClientId());
        formData.add("client_secret", properties.getClientSecret());
        formData.add("username", properties.getUsername());
        formData.add("password", properties.getPassword());
        
        try {
            JsonNode response = salesforceWebClient
                .post()
                .uri(properties.getLoginUrl() + "/services/oauth2/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(formData)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .timeout(Duration.ofSeconds(properties.getTimeout()))
                .block();
            
            if (response != null) {
                this.accessToken = response.get("access_token").asText();
                this.instanceUrl = response.get("instance_url").asText();
                log.info("Authentication successful. Instance URL: {}", instanceUrl);
            } else {
                throw new SalesforceIntegrationException("Empty response from Salesforce authentication");
            }
            
        } catch (WebClientResponseException e) {
            log.error("Authentication failed with status: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new SalesforceIntegrationException("Failed to authenticate with Salesforce", e);
        } catch (Exception e) {
            log.error("Unexpected error during authentication", e);
            throw new SalesforceIntegrationException("Unexpected error during Salesforce authentication", e);
        }
    }
    
    @CircuitBreaker(name = "salesforce", fallbackMethod = "fallbackQuery")
    @Retry(name = "salesforce")
    public List<CompaniaResponse> queryByPais(String pais) {
        ensureAuthenticated();
        
        String soql = String.format(
            "SELECT Id, Name, OwnerId, IsDeleted, CurrencyIsoCode, " +
            "CreatedDate, CreatedById, LastModifiedDate, LastModifiedById, " +
            "SystemModstamp, LastActivityDate, LastViewedDate, LastReferencedDate, " +
            "Codigo__c, Estado__c, Pais__c, Direccion__c, Telefono__c, Acronimo__c, " +
            "Integracion_ERP__c, Habilitar_seleccion_comprobante__c, " +
            "Habilitar_Comprobante_Preventa__c, Habilitar_Impuestos__c, Impuesto__c, " +
            "Habilitar_extra_modulo__c, Habilitar_linea_de_credito__c, " +
            "Acceso__c, Correo_compania__c, Enviar_a_Oracle__c, Enviar_a_Siesa__c, SELLIN__c " +
            "FROM Compania__c WHERE Pais__c = '%s'",
            pais
        );
        
        return executeQuery(soql);
    }
    
    @CircuitBreaker(name = "salesforce", fallbackMethod = "fallbackQueryById")
    @Retry(name = "salesforce")
    public CompaniaResponse queryById(String id) {
        ensureAuthenticated();
        
        String soql = String.format(
            "SELECT Id, Name, OwnerId, IsDeleted, CurrencyIsoCode, " +
            "CreatedDate, CreatedById, LastModifiedDate, LastModifiedById, " +
            "SystemModstamp, LastActivityDate, LastViewedDate, LastReferencedDate, " +
            "Codigo__c, Estado__c, Pais__c, Direccion__c, Telefono__c, Acronimo__c, " +
            "Integracion_ERP__c, Habilitar_seleccion_comprobante__c, " +
            "Habilitar_Comprobante_Preventa__c, Habilitar_Impuestos__c, Impuesto__c, " +
            "Habilitar_extra_modulo__c, Habilitar_linea_de_credito__c, " +
            "Acceso__c, Correo_compania__c, Enviar_a_Oracle__c, Enviar_a_Siesa__c, SELLIN__c " +
            "FROM Compania__c WHERE Id = '%s'",
            id
        );
        
        List<CompaniaResponse> results = executeQuery(soql);
        
        if (results.isEmpty()) {
            return null;
        }
        
        return results.get(0);
    }
    
    private List<CompaniaResponse> executeQuery(String soql) {
        try {
            log.debug("Executing SOQL: {}", soql);
            
            CompaniaResponse.QueryResult result = salesforceWebClient
                .get()
                .uri(uriBuilder -> uriBuilder
                    .scheme("https")
                    .host(instanceUrl.replace("https://", "").replace("http://", ""))
                    .path("/services/data/v59.0/query")
                    .queryParam("q", soql)
                    .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(CompaniaResponse.QueryResult.class)
                .timeout(Duration.ofSeconds(properties.getTimeout()))
                .block();
            
            if (result != null && result.getRecords() != null) {
                log.info("Query returned {} records", result.getTotalSize());
                return result.getRecords();
            }
            
            return Collections.emptyList();
            
        } catch (WebClientResponseException e) {
            if (e.getStatusCode().value() == 401) {
                log.warn("Access token expired, re-authenticating...");
                authenticate();
                return executeQuery(soql);
            }
            log.error("Query failed with status: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new SalesforceIntegrationException("Failed to execute Salesforce query", e);
        } catch (Exception e) {
            log.error("Unexpected error during query execution", e);
            throw new SalesforceIntegrationException("Unexpected error during Salesforce query", e);
        }
    }
    
    private void ensureAuthenticated() {
        if (accessToken == null || instanceUrl == null) {
            authenticate();
        }
    }
    
    private void fallbackAuthenticate(Exception e) {
        log.error("Circuit breaker opened for authentication. Fallback triggered.", e);
        throw new SalesforceIntegrationException("Salesforce service temporarily unavailable", e);
    }
    
    private List<CompaniaResponse> fallbackQuery(String pais, Exception e) {
        log.error("Circuit breaker opened for query by pais. Fallback triggered.", e);
        throw new SalesforceIntegrationException("Salesforce service temporarily unavailable", e);
    }
    
    private CompaniaResponse fallbackQueryById(String id, Exception e) {
        log.error("Circuit breaker opened for query by ID. Fallback triggered.", e);
        throw new SalesforceIntegrationException("Salesforce service temporarily unavailable", e);
    }

    @CircuitBreaker(name = "salesforce", fallbackMethod = "fallbackQuerySucursalById")
    @Retry(name = "salesforce")
    public SucursalResponse querySucursalById(String id) {
        ensureAuthenticated();

        String soql = String.format(
            "SELECT Id, IsDeleted, Name, CurrencyIsoCode, " +
            "CreatedDate, CreatedById, LastModifiedDate, LastModifiedById, " +
            "SystemModstamp, LastActivityDate, LastViewedDate, LastReferencedDate, " +
            "Compania__c, Codigo__c, Codigo_de_Compania__c, Codigo_Unico__c, Pais__c, " +
            "Almacenista__c, Eje_Territorial__c, Habilitar_Extra_Modulo__c, " +
            "Habilitar_Linea_de_Credito__c, Habilitar_Transferencia_Gratuita__c, " +
            "Tipo_de_Sucursal__c, Enviar_a_Oracle__c, Almacen__c, " +
            "Bloques_de_Envio_de_Pedido__c, Enviar_por_Bloques__c, Bloquear_Envio_ERP__c, " +
            "Limite_Reenvio_Pedidos__c, Minutos_Reeenvio_Pedido_en_Espera__c, " +
            "Reenvio_Pedidos_en_Espera__c, Minutos_Reenvio_Pedido_Default__c, " +
            "Facturacion_Punto_de_Venta__c, Estado__c, Enviar_a_Siesa__c " +
            "FROM Sucursal__c WHERE Id = '%s'",
            id
        );

        List<SucursalResponse> results = executeSucursalQuery(soql);

        if (results.isEmpty()) {
            return null;
        }

        return results.get(0);
    }

    private List<SucursalResponse> executeSucursalQuery(String soql) {
        try {
            log.debug("Executing SOQL for Sucursal: {}", soql);

            SucursalResponse.QueryResult result = salesforceWebClient
                .get()
                .uri(uriBuilder -> uriBuilder
                    .scheme("https")
                    .host(instanceUrl.replace("https://", "").replace("http://", ""))
                    .path("/services/data/v59.0/query")
                    .queryParam("q", soql)
                    .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(SucursalResponse.QueryResult.class)
                .timeout(Duration.ofSeconds(properties.getTimeout()))
                .block();

            if (result != null && result.getRecords() != null) {
                log.info("Sucursal query returned {} records", result.getTotalSize());
                return result.getRecords();
            }

            return Collections.emptyList();

        } catch (WebClientResponseException e) {
            if (e.getStatusCode().value() == 401) {
                log.warn("Access token expired, re-authenticating...");
                authenticate();
                return executeSucursalQuery(soql);
            }
            log.error("Sucursal query failed with status: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new SalesforceIntegrationException("Failed to execute Salesforce Sucursal query", e);
        } catch (Exception e) {
            log.error("Unexpected error during Sucursal query execution", e);
            throw new SalesforceIntegrationException("Unexpected error during Salesforce Sucursal query", e);
        }
    }

    private SucursalResponse fallbackQuerySucursalById(String id, Exception e) {
        log.error("Circuit breaker opened for Sucursal query by ID. Fallback triggered.", e);
        throw new SalesforceIntegrationException("Salesforce service temporarily unavailable", e);
    }
}
