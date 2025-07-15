package com.portfolio.portfolio.giftorder.service;

import com.portfolio.portfolio.giftorder.entity.Cart;
import com.portfolio.portfolio.giftorder.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final CartRepository cartRepository;

    /**
     * 고객의 장바구니 조회 (없으면 새로 생성)
     */
    public Cart getOrCreateCart(Long customerId) {
        return cartRepository.findByCustomerId(customerId)
                .orElseGet(() -> {
                    Cart newCart = new Cart(customerId);
                    return cartRepository.save(newCart);
                });
    }

    /**
     * 장바구니 저장/업데이트
     */
    public Cart saveCart(Cart cart) {
        cart.calculateTotal();
        return cartRepository.save(cart);
    }

    /**
     * 장바구니 비우기
     */
    public void clearCart(Long customerId) {
        cartRepository.findByCustomerId(customerId)
                .ifPresent(cart -> {
                    cart.clear();
                    cartRepository.save(cart);
                });
    }

    /**
     * 장바구니 삭제 (주문 완료 후)
     */
    public void deleteCart(Long customerId) {
        cartRepository.findByCustomerId(customerId)
                .ifPresent(cartRepository::delete);
    }

    /**
     * 오래된 장바구니 정리 (스케줄러에서 사용)
     */
    public void cleanupOldCarts() {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(30);
        cartRepository.deleteOldCarts(cutoffDate);

        // 비어있는 장바구니도 7일 후 삭제
        LocalDateTime emptyCutoff = LocalDateTime.now().minusDays(7);
        cartRepository.deleteEmptyCarts(emptyCutoff);
    }
}