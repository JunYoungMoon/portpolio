package com.portfolio.portfolio.hls;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mock")
public class MockHlsController {

    @GetMapping("/**")
    public ResponseEntity<String> serveMockM3u8(HttpServletRequest request) {
        String requestPath = request.getRequestURI();

        if (requestPath.endsWith("index.m3u8")) {
            String mockM3u8 = generateMockM3u8();
            return ResponseEntity.ok()
                    .header("Content-Type", "application/vnd.apple.mpegurl")
                    .body(mockM3u8);
        }

        if (requestPath.endsWith(".ts")) {
            // Mock TS segment
            return ResponseEntity.ok()
                    .header("Content-Type", "video/mp2t")
                    .body("mock-ts-content");
        }

        return ResponseEntity.notFound().build();
    }

    private String generateMockM3u8() {
        return "#EXTM3U\n" +
                "#EXT-X-VERSION:3\n" +
                "#EXT-X-TARGETDURATION:10\n" +
                "#EXT-X-MEDIA-SEQUENCE:0\n" +
                "#EXTINF:9.009,\n" +
                "http://localhost:3041/hls/segment0.ts\n" +
                "#EXTINF:9.009,\n" +
                "http://localhost:3041/hls/segment1.ts\n" +
                "#EXTINF:9.009,\n" +
                "http://localhost:3041/hls/segment2.ts\n" +
                "#EXT-X-ENDLIST\n";
    }
}
