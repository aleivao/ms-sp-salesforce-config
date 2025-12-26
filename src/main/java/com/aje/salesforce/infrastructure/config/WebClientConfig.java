package com.aje.salesforce.infrastructure.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {
    
    private final SalesforceProperties properties;
    
    @Bean
    public WebClient salesforceWebClient() {
        ConnectionProvider connectionProvider = ConnectionProvider.builder("salesforce-pool")
            .maxConnections(properties.getMaxConnections())
            .maxIdleTime(Duration.ofSeconds(20))
            .maxLifeTime(Duration.ofSeconds(60))
            .pendingAcquireTimeout(Duration.ofSeconds(properties.getConnectionTimeout()))
            .build();
        
        HttpClient httpClient = HttpClient.create(connectionProvider)
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, properties.getConnectionTimeout() * 1000)
            .responseTimeout(Duration.ofSeconds(properties.getTimeout()))
            .doOnConnected(conn -> conn
                .addHandlerLast(new ReadTimeoutHandler(properties.getTimeout(), TimeUnit.SECONDS))
                .addHandlerLast(new WriteTimeoutHandler(properties.getTimeout(), TimeUnit.SECONDS))
            );
        
        ExchangeStrategies strategies = ExchangeStrategies.builder()
            .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024))
            .build();
        
        return WebClient.builder()
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .exchangeStrategies(strategies)
            .build();
    }
}
