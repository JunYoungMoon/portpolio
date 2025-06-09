package com.portfolio.portfolio.hls;

import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;

@Configuration
public class HttpClientConfig {

    @Bean
    public RestTemplate restTemplate() {
        // Connection configuration with all timeouts
        ConnectionConfig connectionConfig = ConnectionConfig.custom()
                .setValidateAfterInactivity(TimeValue.ofSeconds(2)) // Validate connections after 2 seconds
                .setTimeToLive(TimeValue.ofMinutes(10)) // Connection TTL
                .setConnectTimeout(Timeout.ofSeconds(5)) // Connection timeout
                .setSocketTimeout(Timeout.ofSeconds(30)) // Socket timeout
                .build();

        // Socket configuration
        SocketConfig socketConfig = SocketConfig.custom()
                .setSoTimeout(Timeout.ofSeconds(30)) // Socket timeout
                .build();

        // Connection pooling configuration
        PoolingHttpClientConnectionManager connectionManager =
                PoolingHttpClientConnectionManagerBuilder.create()
                        .setMaxConnTotal(100) // Maximum total connections
                        .setMaxConnPerRoute(20) // Maximum connections per route
                        .setDefaultConnectionConfig(connectionConfig) // Apply connection config
                        .setDefaultSocketConfig(socketConfig) // Apply socket config
                        .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setKeepAliveStrategy((response, context) -> TimeValue.ofMinutes(5)) // Keep alive for 5 minutes
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setResponseTimeout(Timeout.ofSeconds(30)) // Response timeout
                        .build())
                .build();

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setHttpClient(httpClient);

        return new RestTemplate(factory);
    }
}