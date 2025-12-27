package com.aje.salesforce.infrastructure.adapter.out.salesforce.client;

import com.aje.salesforce.domain.exception.SalesforceIntegrationException;
import com.aje.salesforce.infrastructure.adapter.out.salesforce.response.CompaniaResponse;
import com.aje.salesforce.infrastructure.config.SalesforceProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import org.mockito.ArgumentCaptor;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.DefaultUriBuilderFactory;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("SalesforceClient Tests")
class SalesforceClientTest {

    @Mock
    private WebClient webClient;

    @Mock
    private SalesforceProperties properties;

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestBodySpec requestBodySpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    private SalesforceClient salesforceClient;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        when(properties.getLoginUrl()).thenReturn("https://test.salesforce.com");
        when(properties.getUsername()).thenReturn("test@test.com");
        when(properties.getPassword()).thenReturn("testpass");
        when(properties.getClientId()).thenReturn("testclientid");
        when(properties.getClientSecret()).thenReturn("testclientsecret");
        when(properties.getTimeout()).thenReturn(10000);

        salesforceClient = new SalesforceClient(webClient, properties);
    }

    @Test
    @DisplayName("Should authenticate successfully")
    void shouldAuthenticateSuccessfully() throws Exception {
        String jsonResponse = "{\"access_token\":\"test-token\",\"instance_url\":\"https://instance.salesforce.com\"}";
        JsonNode authNode = objectMapper.readTree(jsonResponse);

        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.contentType(any(MediaType.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(JsonNode.class)).thenReturn(Mono.just(authNode));

        salesforceClient.authenticate();

        String token = (String) ReflectionTestUtils.getField(salesforceClient, "accessToken");
        String instance = (String) ReflectionTestUtils.getField(salesforceClient, "instanceUrl");

        assertThat(token).isEqualTo("test-token");
        assertThat(instance).isEqualTo("https://instance.salesforce.com");

        verify(webClient).post();
    }

    @Test
    @DisplayName("Should query by pais successfully")
    void shouldQueryByPaisSuccessfully() {
        setupAuth();

        CompaniaResponse companiaResponse = CompaniaResponse.builder()
                .id("123")
                .name("Test")
                .pais("Peru")
                .build();

        CompaniaResponse.QueryResult queryResult = new CompaniaResponse.QueryResult(1, true, List.of(companiaResponse));

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header(anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(CompaniaResponse.QueryResult.class)).thenReturn(Mono.just(queryResult));

        List<CompaniaResponse> results = salesforceClient.queryByPais("Peru");

        assertThat(results).isNotNull();
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getId()).isEqualTo("123");
        verify(webClient).get();
    }

    @Test
    @DisplayName("Should query by id successfully")
    void shouldQueryByIdSuccessfully() {
        setupAuth();

        CompaniaResponse companiaResponse = CompaniaResponse.builder()
                .id("123")
                .name("Test")
                .build();

        CompaniaResponse.QueryResult queryResult = new CompaniaResponse.QueryResult(1, true, List.of(companiaResponse));

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header(anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(CompaniaResponse.QueryResult.class)).thenReturn(Mono.just(queryResult));

        CompaniaResponse result = salesforceClient.queryById("123");

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("123");
        verify(webClient).get();
    }

    @Test
    @DisplayName("Should return null when no company found by id")
    void shouldReturnNullWhenNoCompanyFoundById() {
        setupAuth();

        CompaniaResponse.QueryResult queryResult = new CompaniaResponse.QueryResult(0, true, Collections.emptyList());

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header(anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(CompaniaResponse.QueryResult.class)).thenReturn(Mono.just(queryResult));

        CompaniaResponse result = salesforceClient.queryById("999");

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Should return empty list when no companies found by pais")
    void shouldReturnEmptyListWhenNoCompaniesFoundByPais() {
        setupAuth();

        CompaniaResponse.QueryResult queryResult = new CompaniaResponse.QueryResult(0, true, Collections.emptyList());

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header(anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(CompaniaResponse.QueryResult.class)).thenReturn(Mono.just(queryResult));

        List<CompaniaResponse> results = salesforceClient.queryByPais("Unknown");

        assertThat(results).isEmpty();
    }

    private void setupAuth() {
        ReflectionTestUtils.setField(salesforceClient, "accessToken", "test-token");
        ReflectionTestUtils.setField(salesforceClient, "instanceUrl", "https://instance.salesforce.com");
    }

    @Test
    @DisplayName("Should throw exception when authentication response is null")
    void shouldThrowExceptionWhenAuthResponseIsNull() {
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.contentType(any(MediaType.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(JsonNode.class)).thenReturn(Mono.justOrEmpty(null));

        SalesforceIntegrationException exception = assertThrows(
                SalesforceIntegrationException.class,
                () -> salesforceClient.authenticate()
        );
        assertThat(exception.getMessage()).containsAnyOf(
                "Empty response from Salesforce authentication",
                "Unexpected error during Salesforce authentication"
        );
    }

    @Test
    @DisplayName("Should throw exception when authentication fails with WebClientResponseException")
    void shouldThrowExceptionWhenAuthFailsWithWebClientResponseException() {
        WebClientResponseException exception = WebClientResponseException.create(
                HttpStatus.UNAUTHORIZED.value(),
                "Unauthorized",
                HttpHeaders.EMPTY,
                "Invalid credentials".getBytes(StandardCharsets.UTF_8),
                StandardCharsets.UTF_8
        );

        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.contentType(any(MediaType.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(JsonNode.class)).thenReturn(Mono.error(exception));

        assertThatThrownBy(() -> salesforceClient.authenticate())
                .isInstanceOf(SalesforceIntegrationException.class)
                .hasMessageContaining("Failed to authenticate with Salesforce");
    }

    @Test
    @DisplayName("Should throw exception when authentication fails with generic exception")
    void shouldThrowExceptionWhenAuthFailsWithGenericException() {
        RuntimeException exception = new RuntimeException("Connection failed");

        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.contentType(any(MediaType.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(JsonNode.class)).thenReturn(Mono.error(exception));

        assertThatThrownBy(() -> salesforceClient.authenticate())
                .isInstanceOf(SalesforceIntegrationException.class)
                .hasMessageContaining("Unexpected error during Salesforce authentication");
    }

    @Test
    @DisplayName("Should throw exception when query fails with non-401 WebClientResponseException")
    void shouldThrowExceptionWhenQueryFailsWithNon401Error() {
        setupAuth();

        WebClientResponseException exception = WebClientResponseException.create(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                HttpHeaders.EMPTY,
                "Server error".getBytes(StandardCharsets.UTF_8),
                StandardCharsets.UTF_8
        );

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header(anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(CompaniaResponse.QueryResult.class)).thenReturn(Mono.error(exception));

        assertThatThrownBy(() -> salesforceClient.queryByPais("Peru"))
                .isInstanceOf(SalesforceIntegrationException.class)
                .hasMessageContaining("Failed to execute Salesforce query");
    }

    @Test
    @DisplayName("Should throw exception when query fails with generic exception")
    void shouldThrowExceptionWhenQueryFailsWithGenericException() {
        setupAuth();

        RuntimeException exception = new RuntimeException("Timeout");

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header(anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(CompaniaResponse.QueryResult.class)).thenReturn(Mono.error(exception));

        assertThatThrownBy(() -> salesforceClient.queryByPais("Peru"))
                .isInstanceOf(SalesforceIntegrationException.class)
                .hasMessageContaining("Unexpected error during Salesforce query");
    }

    @Test
    @DisplayName("Should return empty list when query result is null")
    void shouldReturnEmptyListWhenQueryResultIsNull() {
        setupAuth();

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header(anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(CompaniaResponse.QueryResult.class)).thenReturn(Mono.empty());

        List<CompaniaResponse> results = salesforceClient.queryByPais("Peru");

        assertThat(results).isEmpty();
    }

    @Test
    @DisplayName("Should return empty list when query result records is null")
    void shouldReturnEmptyListWhenQueryResultRecordsIsNull() {
        setupAuth();

        CompaniaResponse.QueryResult queryResult = new CompaniaResponse.QueryResult(0, true, null);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header(anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(CompaniaResponse.QueryResult.class)).thenReturn(Mono.just(queryResult));

        List<CompaniaResponse> results = salesforceClient.queryByPais("Peru");

        assertThat(results).isEmpty();
    }

    @Test
    @DisplayName("Should authenticate when token is null and query is called")
    void shouldAuthenticateWhenTokenIsNullAndQueryIsCalled() throws Exception {
        // Don't call setupAuth() - token should be null

        String jsonResponse = "{\"access_token\":\"new-token\",\"instance_url\":\"https://instance.salesforce.com\"}";
        JsonNode authNode = objectMapper.readTree(jsonResponse);

        // Setup auth mock
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.contentType(any(MediaType.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(JsonNode.class)).thenReturn(Mono.just(authNode));

        // Setup query mock
        CompaniaResponse companiaResponse = CompaniaResponse.builder()
                .id("123")
                .name("Test")
                .build();

        CompaniaResponse.QueryResult queryResult = new CompaniaResponse.QueryResult(1, true, List.of(companiaResponse));

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header(anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(responseSpec.bodyToMono(CompaniaResponse.QueryResult.class)).thenReturn(Mono.just(queryResult));

        List<CompaniaResponse> results = salesforceClient.queryByPais("Peru");

        assertThat(results).hasSize(1);
        verify(webClient).post(); // Verify authenticate was called
        verify(webClient).get();
    }

    @Test
    @DisplayName("Should re-authenticate on 401 error and retry query")
    void shouldReAuthenticateOn401ErrorAndRetryQuery() throws Exception {
        setupAuth();

        WebClientResponseException exception401 = WebClientResponseException.create(
                HttpStatus.UNAUTHORIZED.value(),
                "Unauthorized",
                HttpHeaders.EMPTY,
                "Token expired".getBytes(StandardCharsets.UTF_8),
                StandardCharsets.UTF_8
        );

        String jsonResponse = "{\"access_token\":\"new-token\",\"instance_url\":\"https://instance.salesforce.com\"}";
        JsonNode authNode = objectMapper.readTree(jsonResponse);

        CompaniaResponse companiaResponse = CompaniaResponse.builder()
                .id("123")
                .name("Test")
                .build();
        CompaniaResponse.QueryResult queryResult = new CompaniaResponse.QueryResult(1, true, List.of(companiaResponse));

        // First call throws 401, second succeeds
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header(anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(CompaniaResponse.QueryResult.class))
                .thenReturn(Mono.error(exception401))
                .thenReturn(Mono.just(queryResult));

        // Auth mock for re-authentication
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.contentType(any(MediaType.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(responseSpec.bodyToMono(JsonNode.class)).thenReturn(Mono.just(authNode));

        List<CompaniaResponse> results = salesforceClient.queryByPais("Peru");

        assertThat(results).hasSize(1);
        verify(webClient).post(); // Verify re-authenticate was called
        verify(webClient, times(2)).get(); // Query was retried
    }

    @Test
    @DisplayName("Should throw exception from fallbackAuthenticate")
    void shouldThrowExceptionFromFallbackAuthenticate() throws Exception {
        Method fallbackMethod = SalesforceClient.class.getDeclaredMethod("fallbackAuthenticate", Exception.class);
        fallbackMethod.setAccessible(true);

        RuntimeException cause = new RuntimeException("Original error");

        try {
            fallbackMethod.invoke(salesforceClient, cause);
        } catch (Exception e) {
            assertThat(e.getCause()).isInstanceOf(SalesforceIntegrationException.class);
            assertThat(e.getCause().getMessage()).contains("temporarily unavailable");
        }
    }

    @Test
    @DisplayName("Should throw exception from fallbackQuery")
    void shouldThrowExceptionFromFallbackQuery() throws Exception {
        Method fallbackMethod = SalesforceClient.class.getDeclaredMethod("fallbackQuery", String.class, Exception.class);
        fallbackMethod.setAccessible(true);

        RuntimeException cause = new RuntimeException("Original error");

        try {
            fallbackMethod.invoke(salesforceClient, "Peru", cause);
        } catch (Exception e) {
            assertThat(e.getCause()).isInstanceOf(SalesforceIntegrationException.class);
            assertThat(e.getCause().getMessage()).contains("temporarily unavailable");
        }
    }

    @Test
    @DisplayName("Should throw exception from fallbackQueryById")
    void shouldThrowExceptionFromFallbackQueryById() throws Exception {
        Method fallbackMethod = SalesforceClient.class.getDeclaredMethod("fallbackQueryById", String.class, Exception.class);
        fallbackMethod.setAccessible(true);

        RuntimeException cause = new RuntimeException("Original error");

        try {
            fallbackMethod.invoke(salesforceClient, "123", cause);
        } catch (Exception e) {
            assertThat(e.getCause()).isInstanceOf(SalesforceIntegrationException.class);
            assertThat(e.getCause().getMessage()).contains("temporarily unavailable");
        }
    }

    @Test
    @DisplayName("Should build correct URI in executeQuery lambda")
    @SuppressWarnings("unchecked")
    void shouldBuildCorrectUriInExecuteQueryLambda() {
        setupAuth();

        CompaniaResponse companiaResponse = CompaniaResponse.builder()
                .id("123")
                .name("Test")
                .build();
        CompaniaResponse.QueryResult queryResult = new CompaniaResponse.QueryResult(1, true, List.of(companiaResponse));

        ArgumentCaptor<Function<UriBuilder, URI>> uriCaptor = ArgumentCaptor.forClass(Function.class);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(uriCaptor.capture())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header(anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(CompaniaResponse.QueryResult.class)).thenReturn(Mono.just(queryResult));

        salesforceClient.queryByPais("Peru");

        // Execute the captured lambda to cover the URI building code
        Function<UriBuilder, URI> uriFunction = uriCaptor.getValue();
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
        UriBuilder uriBuilder = factory.builder();

        URI uri = uriFunction.apply(uriBuilder);

        assertThat(uri).isNotNull();
        assertThat(uri.toString()).contains("/services/data/v59.0/query");
        assertThat(uri.toString()).contains("instance.salesforce.com");
    }
}
