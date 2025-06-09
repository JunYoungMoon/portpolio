package com.portfolio.portfolio.pg;

import com.portfolio.portfolio.pg.dto.PaygateRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/mock")
@Slf4j
public class MockController {

    // 모든 HTTP 메서드를 허용하는 디버깅용 엔드포인트 추가
    @RequestMapping(value = "/**", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    public ResponseEntity<String> debugAllRequests(HttpServletRequest request, @RequestBody(required = false) String body) {
        log.info("=== Mock Controller 요청 수신 ===");
        log.info("Method: {}", request.getMethod());
        log.info("URI: {}", request.getRequestURI());
        log.info("Query: {}", request.getQueryString());
        log.info("Headers: {}", Collections.list(request.getHeaderNames()));
        log.info("Body: {}", body);
        log.info("=====================================");

        // 특정 경로에 따라 응답 분기
        if (request.getRequestURI().contains("/pg/request")) {
            return ResponseEntity.ok("{\"payment_url\":\"http://localhost:19093/mock-payment-page\"}");
        } else if (request.getRequestURI().contains("/blockchain/krw_buy")) {
            return ResponseEntity.ok("{\"status\":\"success\",\"message\":\"소유권 이전 완료\"}");
        }

        return ResponseEntity.ok("{\"message\":\"Mock response\"}");
    }

    @PostMapping("/pg/request")
    public ResponseEntity<String> mockPgRequest(@RequestBody PaygateRequestDto request) {
        log.info("Mock PG 요청 수신: {}", request);
        return ResponseEntity.ok("{\"payment_url\":\"http://localhost:19093/mock-payment-page\"}");
    }

    @PostMapping("/blockchain/krw_buy")
    public ResponseEntity<String> mockBlockchainTransfer(@RequestBody Object request) {
        log.info("Mock 블록체인 요청 수신: {}", request);
        return ResponseEntity.ok("{\"status\":\"success\",\"message\":\"소유권 이전 완료\"}");
    }

    @GetMapping("/mock-payment-page")
    public String mockPaymentPage() {
        return """
                <html>
                <body>
                    <h1>Mock 결제 페이지</h1>
                    <button onclick="window.close()">결제 완료</button>
                </body>
                </html>
                """;
    }
}