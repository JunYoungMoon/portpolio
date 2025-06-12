package com.portfolio.portfolio.ddd.presentation.dto;

public class CreateOrderResponse {
    private final Long orderId;

    public CreateOrderResponse(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderId() { return orderId; }
}
