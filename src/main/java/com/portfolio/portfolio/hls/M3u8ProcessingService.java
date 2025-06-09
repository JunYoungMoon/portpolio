package com.portfolio.portfolio.hls;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
@AllArgsConstructor
public class M3u8ProcessingService {

    private AppProperties appProperties;
    private AesEncryptionUtil encryptionUtil;
    private RestTemplate restTemplate;

    public ResponseEntity<String> processM3u8(String name, boolean useOriginal, int timeout) {
        try {
            String path = useOriginal ? name :
                    String.format("/%s/%s/index.m3u8",
                            appProperties.getTargetPath(),
                            URLEncoder.encode(name, StandardCharsets.UTF_8));

            String url = String.format("http://%s:%d%s",
                    appProperties.getTargetAddress(),
                    appProperties.getTargetPort(),
                    path);

            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            String m3u8Content = response.getBody();

            if (m3u8Content == null) {
                throw new RuntimeException("Empty M3U8 content");
            }

            String[] lines = m3u8Content.split("\n");
            StringBuilder processedContent = new StringBuilder(m3u8Content.length() * 2);
            String targetServer = String.format("%s:%d",
                    appProperties.getTargetAddress(),
                    appProperties.getTargetPort());

            int startAddressIndex = 0;
            int processedLines = 0;

            for (int i = 0; i < lines.length - 2; i++) {
                String line = lines[i];

                if (line.contains(targetServer)) {
                    if (startAddressIndex == 0) {
                        startAddressIndex = i;
                    }

                    String[] parts = appProperties.getTargetPath().isEmpty() ?
                            line.split(targetServer + "/") :
                            line.split(targetServer + "/" + appProperties.getTargetPath() + "/");

                    if (parts.length > 1) {
                        String pathToEncrypt = parts[1];
                        String encrypted = appProperties.isUseEncrypt() ?
                                encryptionUtil.encrypt(appProperties.getSalt(), pathToEncrypt) :
                                pathToEncrypt;

                        String front = parts[0]
                                .replace("http://", "")
                                .replace("https://", "");

                        processedContent.append(appProperties.getMyAddress())
                                .append("/")
                                .append(appProperties.getTargetPath())
                                .append("/")
                                .append(encrypted);
                    } else {
                        processedContent.append(line);
                    }

                    processedLines++;
                } else {
                    processedContent.append(line);
                }

                processedContent.append("\n");

                // Timeout handling
                if (timeout > 0 && startAddressIndex > 0 && processedLines >= timeout) {
                    break;
                }
            }

            // Add the last line
            if (lines.length >= 2) {
                processedContent.append(lines[lines.length - 2]);
            }

            log.info("Processed M3U8 content: {}", processedContent.toString());

            HttpHeaders headers = new HttpHeaders();
            response.getHeaders().forEach((key, value) -> {
                if (!HttpHeaders.TRANSFER_ENCODING.equalsIgnoreCase(key)) {
                    headers.put(key, value);
                }
            });

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(processedContent.toString());

        } catch (Exception e) {
            log.error("Error processing M3U8: ", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"status\":\"fail\",\"msg\":\"" + e.getMessage() + "\"}");
        }
    }
}
