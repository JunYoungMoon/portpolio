package com.portfolio.portfolio.pg.repository.r2dbc;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface TradingRepository extends ReactiveCrudRepository<TradingEntity, Long> {
    Mono<TradingEntity> findByTradingIdxAndStatus(Long tradingIdx, String status);
}