package com.portfolio.portfolio.giftorder.entity;

import lombok.Data;

@Data
public class Cart {
    private int galbiQuantity = 0;
    private int spamQuantity = 0;
    private int totalAmount = 0;

    private static final int GALBI_PRICE = 50000;
    private static final int SPAM_PRICE = 30000;

    public void calculateTotal() {
        this.totalAmount = (galbiQuantity * GALBI_PRICE) + (spamQuantity * SPAM_PRICE);
    }

    public boolean isEmpty() {
        return galbiQuantity == 0 && spamQuantity == 0;
    }
}
