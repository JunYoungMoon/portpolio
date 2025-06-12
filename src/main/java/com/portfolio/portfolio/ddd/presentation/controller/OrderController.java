package com.portfolio.portfolio.ddd.presentation.controller;

import com.portfolio.portfolio.ddd.application.CreateOrderCommand;
import com.portfolio.portfolio.ddd.application.service.OrderApplicationService;
import com.portfolio.portfolio.ddd.domain.OrderType;
import com.portfolio.portfolio.ddd.presentation.dto.CreateOrderRequest;
import com.portfolio.portfolio.ddd.presentation.dto.CreateOrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderApplicationService orderApplicationService;

    public OrderController(OrderApplicationService orderApplicationService) {
        this.orderApplicationService = orderApplicationService;
    }

    @PostMapping
    public ResponseEntity<CreateOrderResponse> createOrder(@RequestBody CreateOrderRequest request) {
        CreateOrderCommand command = new CreateOrderCommand(
                request.getUserId(),
                OrderType.valueOf(request.getType()),
                request.getSymbol(),
                request.getPrice(),
                request.getQuantity()
        );

        Long orderId = orderApplicationService.createOrder(command);

        return ResponseEntity.ok(new CreateOrderResponse(orderId));
    }
}
