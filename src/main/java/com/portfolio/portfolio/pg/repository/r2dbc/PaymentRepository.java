package com.portfolio.portfolio.pg.repository.r2dbc;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface PaymentRepository extends ReactiveCrudRepository<LocalPgEntity, Long> {
    Mono<LocalPgEntity> findByTid(String tid);
}