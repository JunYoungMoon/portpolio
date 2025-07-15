package com.portfolio.portfolio.giftorder.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "carts")
@Data
@NoArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long customerId;

    private int galbiQuantity = 0;
    private int spamQuantity = 0;
    private int totalAmount = 0;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private static final int GALBI_PRICE = 50000;
    private static final int SPAM_PRICE = 30000;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void calculateTotal() {
        this.totalAmount = (galbiQuantity * GALBI_PRICE) + (spamQuantity * SPAM_PRICE);
    }

    public boolean isEmpty() {
        return galbiQuantity == 0 && spamQuantity == 0;
    }

    public int getTotalItemCount() {
        return galbiQuantity + spamQuantity;
    }

    public void clear() {
        this.galbiQuantity = 0;
        this.spamQuantity = 0;
        this.totalAmount = 0;
    }

    // 생성자
    public Cart(Long customerId) {
        this.customerId = customerId;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
