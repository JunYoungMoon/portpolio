package com.portfolio.portfolio.pg.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCallbackDto {
    private String userId;
    private String amount;
    private String tid;
    private String orderNo;
    private String payhash;
    private String customParameter;
}
