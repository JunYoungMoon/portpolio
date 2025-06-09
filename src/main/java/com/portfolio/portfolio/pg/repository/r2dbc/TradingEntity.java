package com.portfolio.portfolio.pg.repository.r2dbc;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table("trading")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TradingEntity {
    @Id
    private Long id;
    private Long tradingIdx;
    private String status;
    private BigDecimal price;
    private Long sellerIdx;
    private Long buyerIdx;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
