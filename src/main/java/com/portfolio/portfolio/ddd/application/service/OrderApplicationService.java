package com.portfolio.portfolio.ddd.application.service;

import com.portfolio.portfolio.ddd.application.CreateOrderCommand;
import com.portfolio.portfolio.ddd.domain.OrderType;
import com.portfolio.portfolio.ddd.domain.model.entity.DDDOrder;
import com.portfolio.portfolio.ddd.domain.model.entity.DDDUser;
import com.portfolio.portfolio.ddd.domain.model.vo.Price;
import com.portfolio.portfolio.ddd.domain.model.vo.Quantity;
import com.portfolio.portfolio.ddd.domain.repository.OrderRepository;
import com.portfolio.portfolio.ddd.domain.repository.UserRepository;
import com.portfolio.portfolio.ddd.domain.service.OrderMatchingService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderApplicationService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderMatchingService orderMatchingService;

    public OrderApplicationService(OrderRepository orderRepository,
                                   UserRepository userRepository,
                                   OrderMatchingService orderMatchingService) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.orderMatchingService = orderMatchingService;
    }

    // 유스케이스: 주문 생성
    public Long createOrder(CreateOrderCommand command) {
        DDDUser DDDUser = userRepository.findById(command.getUserId());

        DDDOrder DDDOrder = new DDDOrder(
                command.getUserId(),
                command.getType(),
                command.getSymbol(),
                new Price(command.getPrice(), "USDT"),
                new Quantity(command.getQuantity())
        );

        orderRepository.save(DDDOrder);

        // 매칭 시도
        attemptMatching(DDDOrder);

        return DDDOrder.getOrderId();
    }

    private void attemptMatching(DDDOrder newDDDOrder) {
        List<DDDOrder> pendingDDDOrders = orderRepository.findPendingOrdersBySymbol(newDDDOrder.getSymbol());

        for (DDDOrder existingDDDOrder : pendingDDDOrders) {
            if (canMatch(newDDDOrder, existingDDDOrder)) {
                DDDUser buyer = userRepository.findById(newDDDOrder.getType() == OrderType.BUY ?
                        newDDDOrder.getUserId() : existingDDDOrder.getUserId());
                DDDUser seller = userRepository.findById(newDDDOrder.getType() == OrderType.SELL ?
                        newDDDOrder.getUserId() : existingDDDOrder.getUserId());

                orderMatchingService.matchOrders(
                        newDDDOrder.getType() == OrderType.BUY ? newDDDOrder : existingDDDOrder,
                        newDDDOrder.getType() == OrderType.SELL ? newDDDOrder : existingDDDOrder,
                        buyer, seller
                );
                break;
            }
        }
    }

    private boolean canMatch(DDDOrder DDDOrder1, DDDOrder DDDOrder2) {
        return DDDOrder1.getType() != DDDOrder2.getType() &&
                DDDOrder1.getSymbol().equals(DDDOrder2.getSymbol());
    }
}