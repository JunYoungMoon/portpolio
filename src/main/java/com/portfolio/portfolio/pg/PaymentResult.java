package com.portfolio.portfolio.pg;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentResult {
    private boolean success;
    private String message;

    public static PaymentResult success() {
        return PaymentResult.builder().success(true).build();
    }

    public static PaymentResult failed() {
        return PaymentResult.builder().success(false).build();
    }
}
