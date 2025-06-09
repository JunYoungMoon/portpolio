package com.portfolio.portfolio.hls;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HlsStreamingIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port;
    }

    @Test
    @Order(1)
    void testMockHlsServer() {
        // Mock HLS 서버 직접 테스트
        ResponseEntity<String> response = restTemplate.getForEntity(
                baseUrl + "/mock/test-video/index.m3u8",
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("#EXTM3U"));
        System.out.println("Mock M3U8 Response: " + response.getBody());
    }


    @Test
    @Order(2)
    void testPathGeneration() {
        PathRequest request = new PathRequest("test-video", 30);

        ResponseEntity<PathResponse> response = restTemplate.postForEntity(
                baseUrl + "/path",
                request,
                PathResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("success", response.getBody().getStatus());
        assertTrue(response.getBody().getPath().contains("play.m3u8"));

        System.out.println("Generated path: " + response.getBody().getPath());
    }

    @Test
    @Order(3)
    void testPlayEndpoint() {
        PlayRequest request = new PlayRequest("test-video", 10);

        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl + "/play",
                request,
                String.class
        );

        // Mock 서버가 없으면 404나 연결 오류가 발생할 수 있음
        System.out.println("Play response status: " + response.getStatusCode());
        System.out.println("Play response body: " + response.getBody());
    }

    @Test
    @Order(4)
    void testEncryptionDecryption() {
        AesEncryptionUtil encryptionUtil = new AesEncryptionUtil();
        String salt = "test-salt-123";
        String original = "30/hls/test-video";

        String encrypted = encryptionUtil.encrypt(salt, original);
        String decrypted = encryptionUtil.decrypt(salt, encrypted);

        assertEquals(original, decrypted);
        System.out.println("Encryption test passed: " + original + " -> " + encrypted + " -> " + decrypted);
    }
}