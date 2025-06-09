package com.portfolio.portfolio.hls;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class MockHlsTestConfig {

    @Bean
    @Primary
    public AppProperties mockAppProperties() {
        AppProperties props = new AppProperties();
        props.setServerPort(8080);
        props.setSalt("test-salt-123");
        props.setTargetAddress("localhost");
        props.setTargetPort(3041);
        props.setTargetPath("hls");
        props.setMyAddress("http://localhost:8080");
        props.setUseEncrypt(true);
        props.setUseOriginal(false);
        return props;
    }
}