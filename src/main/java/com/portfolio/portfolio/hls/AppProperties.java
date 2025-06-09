package com.portfolio.portfolio.hls;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "app")
@Data
@Component
public class AppProperties {
    private int serverPort = 8080;
    private String salt;
    private String targetAddress;
    private int targetPort;
    private String targetPath = "";
    private String myAddress;
    private boolean useEncrypt = true;
    private boolean useOriginal = false;
}
