package com.portfolio.portfolio.oop.movie;

public class NoneDiscountPolicy extends DiscountPolicy{

    @Override
    protected Money getDiscountAmount(Screening screening) {
        return Money.ZERO;
    }
}
