package com.portfolio.portfolio.pg;

import com.portfolio.portfolio.nplus1.repository.jpa.UserRepository;
import com.portfolio.portfolio.pg.dto.CancelRequestDto;
import com.portfolio.portfolio.pg.dto.PaygateRequestDto;
import com.portfolio.portfolio.pg.dto.PaymentCallbackDto;
import com.portfolio.portfolio.pg.dto.PaymentRequestDto;
import com.portfolio.portfolio.pg.repository.r2dbc.LocalPgEntity;
import com.portfolio.portfolio.pg.repository.r2dbc.PaymentRepository;
import com.portfolio.portfolio.pg.repository.r2dbc.TradingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {

    private final WebClient webClient;
    private final PaymentRepository paymentRepository;
    private final TradingRepository tradingRepository;

    @Value("${payment.pg.url}")
    private String pgUrl;

    @Value("${payment.pg.code}")
    private String pgCode;

    @Value("${payment.pg.client-id}")
    private String clientId;

    @Value("${payment.pg.key}")
    private String pgKey;

    /**
     * 결제 요청 검증
     */
    public Mono<PaymentRequestDto> validatePaymentRequest(PaymentRequestDto request) {
        if (request.getTradingIdx() == null || request.getUserIdx() == null) {
            return Mono.error(new IllegalArgumentException("필수 정보가 수신되지 않음"));
        }

        return tradingRepository.findByTradingIdxAndStatus(request.getTradingIdx(), "50")
                .switchIfEmpty(Mono.error(new RuntimeException("주문에 해당하는 가격 정보가 없습니다")))
                .map(trading -> {
                    request.setPrice(trading.getPrice());
                    return request;
                });
    }

    /**
     * 페이레터 결제 URL 생성
     */
    public Mono<String> createPaymentUrl(PaymentRequestDto request) {
        String environment = getEnvironment();
        String baseUrl = getBaseUrl(environment);

        PaygateRequestDto paygateRequest = PaygateRequestDto.builder()
                .pgcode(pgCode)
                .clientId(clientId)
                .userId(request.getUserIdx().toString())
                .orderNo(request.getTradingIdx().toString())
                .amount(request.getPrice().toString())
                .productName(request.getTitle())
                .customParameter(createCustomParameter(request))
                .returnUrl(baseUrl + "/music/order_done")
                .callbackUrl(baseUrl + "/api/pg/buy_callback")
                .cancelUrl(baseUrl + "/api/pg/nft_close_pop?device=" + request.getDevice())
                .build();

        return webClient.post()
                .uri(pgUrl + "/request")
                .header("Content-Type", "application/json")
                .header("Authorization", "PLKEY " + pgKey)
                .bodyValue(paygateRequest)
                .retrieve()
                .onStatus(status -> status.isError(), response ->
                        response.bodyToMono(String.class)
                                .map(error -> new RuntimeException("결제 요청 실패: " + error)))
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(10));
    }

    /**
     * 콜백 검증
     */
    public Mono<PaymentCallbackDto> validateCallback(PaymentCallbackDto callback) {
        if (callback == null || callback.getUserId() == null ||
                callback.getAmount() == null || callback.getTid() == null) {
            return Mono.error(new IllegalArgumentException("콜백 데이터가 비어있습니다"));
        }

        String expectedHash = sha256Hex(
                callback.getUserId() + callback.getAmount() + callback.getTid() + pgKey
        );

        if (!expectedHash.equalsIgnoreCase(callback.getPayhash())) {
            return Mono.error(new SecurityException("해시 검증 실패"));
        }

        return Mono.just(callback);
    }

    /**
     * 결제 결과 저장
     */
    public Mono<PaymentResult> savePaymentResult(PaymentCallbackDto callback, BlockchainResult blockchainResult) {
        LocalPgEntity entity = LocalPgEntity.builder()
                .tid(callback.getTid())
                .userId(callback.getUserId())
                .amount(callback.getAmount())
                .orderNo(callback.getOrderNo())
                .status(blockchainResult.isSuccess() ? "Y" : "N")
                .message(blockchainResult.isSuccess() ? "승인 완료[소유권 이전 완료]" : "승인 실패[소유권 이전 실패]")
                .createdAt(LocalDateTime.now())
                .build();

        return paymentRepository.save(entity)
                .map(saved -> PaymentResult.success())
                .onErrorReturn(PaymentResult.failed());
    }

    /**
     * 결제 취소 스케줄링
     */
    public Mono<Void> schedulePaymentCancel(PaymentCallbackDto callback) {
        return paymentRepository.findByTid(callback.getTid())
                .filter(pg -> "N".equals(pg.getStatus()))
                .flatMap(pg -> cancelPayment(callback))
                .then();
    }

    private Mono<String> cancelPayment(PaymentCallbackDto callback) {
        CancelRequestDto cancelRequest = CancelRequestDto.builder()
                .pgcode(pgCode)
                .clientId(clientId)
                .userId(callback.getUserId())
                .tid(callback.getTid())
                .amount(callback.getAmount())
                .ipAddr(getClientIp())
                .build();

        return webClient.post()
                .uri(pgUrl + "/cancel")
                .header("Content-Type", "application/json")
                .header("Authorization", "PLKEY " + pgKey)
                .bodyValue(cancelRequest)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(10))
                .doOnSuccess(response -> log.info("결제 취소 완료: {}", callback.getTid()))
                .doOnError(error -> log.error("결제 취소 실패: {}", callback.getTid(), error));
    }

    private String createCustomParameter(PaymentRequestDto request) {
        return String.format("{ \"device\" : \"%s\", \"lang\" : \"%s\" }",
                request.getDevice(), request.getLang());
    }

    private String getEnvironment() {
        // 환경 설정에 따라 반환
        return "dev"; // 또는 application.yml에서 가져오기
    }

    private String getBaseUrl(String environment) {
        return switch (environment) {
            case "local", "dev" -> "https://kn.sgmchain.com";
            case "prod" -> "https://koong.world";
            default -> "https://kn.sgmchain.com";
        };
    }

    private String getClientIp() {
        // 실제 구현에서는 ServerHttpRequest에서 IP 추출
        return "127.0.0.1";
    }

    private String sha256Hex(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 알고리즘을 찾을 수 없습니다", e);
        }
    }
}
