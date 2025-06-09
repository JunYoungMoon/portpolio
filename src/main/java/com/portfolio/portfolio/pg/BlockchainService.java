package com.portfolio.portfolio.pg;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.portfolio.pg.dto.BlockchainResponseDto;
import com.portfolio.portfolio.pg.dto.BlockchainTransferDto;
import com.portfolio.portfolio.pg.dto.CustomParameterDto;
import com.portfolio.portfolio.pg.dto.PaymentCallbackDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
@Slf4j
@RequiredArgsConstructor
public class BlockchainService {

    private final WebClient webClient;

    /**
     * 블록체인 소유권 이전 처리 (비동기)
     */
    public Mono<BlockchainResult> processOwnershipTransfer(PaymentCallbackDto callback) {
        CustomParameterDto customParam = parseCustomParameter(callback.getCustomParameter());

        BlockchainTransferDto transferRequest = BlockchainTransferDto.builder()
                .payCode(callback.getTid())
                .userIdx(callback.getUserId())
                .tradingIdx(callback.getOrderNo())
                .lang(customParam.getLang())
                .build();

        return webClient.post()
                .uri("http://localhost:19093/mock/blockchain/krw_buy") // 실제 블록체인 API URL
                .bodyValue(transferRequest)
                .retrieve()
                .onStatus(status -> status.isError(), response ->
                        response.bodyToMono(String.class)
                                .map(error -> new RuntimeException("블록체인 호출 실패: " + error)))
                .bodyToMono(BlockchainResponseDto.class)
                .map(response -> "success".equals(response.getStatus()) ?
                        BlockchainResult.success() : BlockchainResult.failed(response.getMessage()))
                .timeout(Duration.ofSeconds(30)) // 블록체인 처리는 시간이 오래 걸릴 수 있음
                .retry(3) // 실패 시 재시도
                .onErrorReturn(BlockchainResult.failed("블록체인 네트워크 오류"));
    }

    private CustomParameterDto parseCustomParameter(String customParameter) {
        try {
            return new ObjectMapper().readValue(customParameter, CustomParameterDto.class);
        } catch (Exception e) {
            log.error("커스텀 파라미터 파싱 실패", e);
            return new CustomParameterDto("web", "ko");
        }
    }
}
