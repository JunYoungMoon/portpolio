package com.portfolio.portfolio.giftorder.service;

import com.portfolio.portfolio.giftorder.entity.Customer;
import com.portfolio.portfolio.giftorder.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public Customer findByNameAndPhone(String name, String phone) {
        return customerRepository.findByNameAndPhone(name, phone);
    }

    public Customer findById(Long id) {
        return customerRepository.findById(id).orElse(null);
    }
}
