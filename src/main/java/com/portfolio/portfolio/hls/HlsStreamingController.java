package com.portfolio.portfolio.hls;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@Slf4j
@AllArgsConstructor
public class HlsStreamingController {

    private AppProperties appProperties;
    private AesEncryptionUtil encryptionUtil;
    private M3u8ProcessingService m3u8ProcessingService;
    private RestTemplate restTemplate;

    // Handle M3U8 requests
    @GetMapping("/**")
    public ResponseEntity<?> handleM3u8Request(HttpServletRequest request) {
        String requestUrl = request.getRequestURI();
        log.info("Request URL: {}", requestUrl);

        String[] urlParts = requestUrl.split("/");
        String lastPart = urlParts[urlParts.length - 1];

        if (lastPart.endsWith("index.m3u8")) {
            return m3u8ProcessingService.processM3u8(requestUrl, true, -1);
        } else if (lastPart.endsWith("play.m3u8")) {
            try {
                String encryptedPath = requestUrl.replace("/play.m3u8", "").substring(1);
                String decrypted = encryptionUtil.decrypt(appProperties.getSalt(), encryptedPath);
                String[] paths = decrypted.split("/");

                int timeout = Integer.parseInt(paths[0]);
                StringBuilder pathBuilder = new StringBuilder();
                for (int i = 1; i < paths.length; i++) {
                    pathBuilder.append("/").append(paths[i]);
                }
                pathBuilder.append("/index.m3u8");

                return m3u8ProcessingService.processM3u8(pathBuilder.toString(), true, timeout);
            } catch (Exception e) {
                log.error("Error processing play.m3u8 request: ", e);
                return ResponseEntity.badRequest()
                        .body("{\"status\":\"fail\",\"msg\":\"" + e.getMessage() + "\"}");
            }
        }

        // Handle other requests (proxy to target server)
        return handleProxyRequest(request);
    }

    // POST endpoint for play
    @PostMapping("/play")
    public ResponseEntity<String> play(@RequestBody PlayRequest playRequest) {
        int timeout = playRequest.getTimeout() != null ? playRequest.getTimeout() : 10;
        return m3u8ProcessingService.processM3u8(playRequest.getName(), false, timeout);
    }

    // POST endpoint for path generation
    @PostMapping("/path")
    public ResponseEntity<PathResponse> generatePath(@RequestBody PathRequest pathRequest) {
        try {
            int timeout = pathRequest.getTimeout() != null ? pathRequest.getTimeout() : -1;
            String pathToEncrypt = String.format("%d/%s/%s",
                    timeout,
                    appProperties.getTargetPath(),
                    URLEncoder.encode(pathRequest.getName(), StandardCharsets.UTF_8));

            String encrypted = encryptionUtil.encrypt(appProperties.getSalt(), pathToEncrypt);
            String generatedPath = String.format("%s/%s/play.m3u8",
                    appProperties.getMyAddress(), encrypted);

            return ResponseEntity.ok(new PathResponse("success", generatedPath));
        } catch (Exception e) {
            log.error("Error generating path: ", e);
            return ResponseEntity.badRequest()
                    .body(new PathResponse("fail", e.getMessage()));
        }
    }

    // Handle proxy requests
    private ResponseEntity<byte[]> handleProxyRequest(HttpServletRequest request) {
        try {
            String requestUrl = request.getRequestURI();
            String queryString = request.getQueryString();

            String targetUrl;
            if (appProperties.isUseEncrypt()) {
                if (requestUrl.startsWith("/koong/")) {
                    String encryptedPart = requestUrl.substring("/koong/".length());
                    String decryptedPath = encryptionUtil.decrypt(appProperties.getSalt(), encryptedPart);
                    targetUrl = String.format("http://%s:%d/koong/%s",
                            appProperties.getTargetAddress(),
                            appProperties.getTargetPort(),
                            URLEncoder.encode(decryptedPath, StandardCharsets.UTF_8));
                } else {
                    return ResponseEntity.notFound().build();
                }
            } else {
                targetUrl = String.format("http://%s:%d%s",
                        appProperties.getTargetAddress(),
                        appProperties.getTargetPort(),
                        requestUrl);
            }

            if (queryString != null) {
                targetUrl += "?" + queryString;
            }

            log.info("Proxying to: {}", targetUrl);

            ResponseEntity<byte[]> response = restTemplate.getForEntity(targetUrl, byte[].class);

            HttpHeaders headers = new HttpHeaders();
            response.getHeaders().forEach((key, value) -> {
                if (!HttpHeaders.TRANSFER_ENCODING.equalsIgnoreCase(key) &&
                        !HttpHeaders.CONNECTION.equalsIgnoreCase(key)) {
                    headers.put(key, value);
                }
            });

            return ResponseEntity.status(response.getStatusCode())
                    .headers(headers)
                    .body(response.getBody());

        } catch (Exception e) {
            log.error("Error handling proxy request: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
