package com.portfolio.portfolio.pg;

import com.portfolio.portfolio.pg.dto.PaymentCallbackDto;
import com.portfolio.portfolio.pg.dto.PaymentRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
@RequestMapping("/api/pg")
@Slf4j
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final BlockchainService blockchainService;

    /**
     * 페이레터 결제 URL 요청 (로컬 결제)
     */
    @PostMapping("/local-reservation")
    public Mono<ResponseEntity<String>> localReservation(@RequestBody PaymentRequestDto request) {
        return paymentService.validatePaymentRequest(request)
                .flatMap(paymentService::createPaymentUrl)
                .map(ResponseEntity::ok)
                .onErrorResume(error -> {
                    log.error("결제 URL 생성 실패", error);
                    return Mono.just(ResponseEntity.badRequest()
                            .body("{\"error\":\"" + error.getMessage() + "\"}"));
                });
    }

    /**
     * 페이레터 콜백 처리 (비동기)
     */
    @PostMapping("/buy-callback")
    public Mono<ResponseEntity<String>> buyCallback(@RequestBody PaymentCallbackDto callback) {
        log.info("페이레터 콜백 수신: {}", callback);

        return paymentService.validateCallback(callback)
                .flatMap(validCallback -> {
                    // 비동기로 블록체인 소유권 이전 처리
                    return blockchainService.processOwnershipTransfer(validCallback)
                            .flatMap(result -> paymentService.savePaymentResult(validCallback, result))
                            .doOnSuccess(success -> log.info("소유권 이전 완료: {}", validCallback.getTid()))
                            .doOnError(error -> {
                                log.error("소유권 이전 실패: {}", validCallback.getTid(), error);
                                // 실패 시 결제 취소 처리는 별도 스케줄러에서 처리
                                paymentService.schedulePaymentCancel(validCallback)
                                        .subscribeOn(Schedulers.boundedElastic())
                                        .subscribe();
                            })
                            .onErrorReturn(PaymentResult.failed());
                })
                .map(result -> ResponseEntity.ok("{\"code\":0, \"message\":\"success\"}"))
                .onErrorReturn(ResponseEntity.ok("{\"code\":9999, \"message\":\"Response data is empty\"}"));
    }
}
