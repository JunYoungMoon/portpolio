package com.portfolio.portfolio.giftorder.repository;

import com.portfolio.portfolio.giftorder.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByNameAndPhone(String name, String phone);
}