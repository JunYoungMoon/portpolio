package com.portfolio.portfolio.giftorder.repository;

import com.portfolio.portfolio.giftorder.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;


import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByCustomerId(Long customerId);

    // 오래된 장바구니 정리 (30일 이상 미사용)
    @Modifying
    @Query("DELETE FROM Cart c WHERE c.updatedAt < :cutoffDate")
    void deleteOldCarts(@Param("cutoffDate") LocalDateTime cutoffDate);

    // 비어있는 장바구니 정리
    @Modifying
    @Query("DELETE FROM Cart c WHERE c.galbiQuantity = 0 AND c.spamQuantity = 0 AND c.updatedAt < :cutoffDate")
    void deleteEmptyCarts(@Param("cutoffDate") LocalDateTime cutoffDate);
}
