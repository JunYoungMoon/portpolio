package com.portfolio.portfolio.ddd.domain.service;

import com.portfolio.portfolio.ddd.domain.model.entity.DDDOrder;
import com.portfolio.portfolio.ddd.domain.model.entity.DDDUser;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OrderMatchingService {

    // 주문 매칭 로직 (Order + User 애그리거트 간 협력)
    public void matchOrders(DDDOrder buyDDDOrder, DDDOrder sellDDDOrder, DDDUser buyer, DDDUser seller) {
        // 비즈니스 규칙 검증
        validateOrderMatching(buyDDDOrder, sellDDDOrder);

        // 여러 애그리거트에 걸친 로직
        executeTrade(buyDDDOrder, sellDDDOrder, buyer, seller);
    }

    private void validateOrderMatching(DDDOrder buyDDDOrder, DDDOrder sellDDDOrder) {
        if (!buyDDDOrder.getSymbol().equals(sellDDDOrder.getSymbol())) {
            throw new IllegalArgumentException("심볼이 일치하지 않습니다");
        }
        if (buyDDDOrder.getPrice().getAmount().compareTo(sellDDDOrder.getPrice().getAmount()) < 0) {
            throw new IllegalArgumentException("가격이 맞지 않습니다");
        }
    }

    private void executeTrade(DDDOrder buyDDDOrder, DDDOrder sellDDDOrder, DDDUser buyer, DDDUser seller) {
        // 잔고 변경
        String[] symbols = buyDDDOrder.getSymbol().split("/");
        String baseCurrency = symbols[0]; // BTC
        String quoteCurrency = symbols[1]; // USDT

        BigDecimal tradeAmount = buyDDDOrder.getQuantity().getValue();
        BigDecimal tradePrice = buyDDDOrder.getPrice().getAmount();

        // 구매자: USDT 차감, BTC 증가
        buyer.withdraw(quoteCurrency, tradePrice.multiply(tradeAmount));
        buyer.deposit(baseCurrency, tradeAmount);

        // 판매자: BTC 차감, USDT 증가
        seller.withdraw(baseCurrency, tradeAmount);
        seller.deposit(quoteCurrency, tradePrice.multiply(tradeAmount));

        // 주문 상태 변경
        buyDDDOrder.execute();
        sellDDDOrder.execute();
    }
}