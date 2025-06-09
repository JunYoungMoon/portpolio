package com.portfolio.portfolio.pg.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDto {
    private Long tradingIdx;
    private Long userIdx;
    private String lang;
    private String title;
    private String device;
    private String sessionCode;
    private BigDecimal price;
}