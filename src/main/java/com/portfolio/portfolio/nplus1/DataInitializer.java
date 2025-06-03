package com.portfolio.portfolio.nplus1;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

// 6. 테스트 데이터 생성을 위한 초기화 클래스
@Component
public class DataInitializer {

    private final UserRepository userRepository;
    private final UserBalanceRepository userBalanceRepository;

    public DataInitializer(UserRepository userRepository, UserBalanceRepository userBalanceRepository) {
        this.userRepository = userRepository;
        this.userBalanceRepository = userBalanceRepository;
    }

    @PostConstruct
    @Transactional
    public void initializeData() {
        // 기존 데이터가 있으면 초기화하지 않음
        if (userRepository.count() > 0) {
            return;
        }

        // 테스트용 사용자 생성
        for (int i = 1; i <= 3; i++) {
            User user = new User();
            // UUID는 자동 생성되므로 별도로 설정하지 않음
            user.setUsername("user" + i);
            user.setEmail("user" + i + "@example.com");
            user.setPassword("password" + i);
            user.setPhone("010-1234-567" + i);
            user.setBankAccountNumber("1234-5678-90" + i);
            user.setRole(i == 1 ? User.UserRole.MASTER : User.UserRole.HUB_MANAGER);

            // 먼저 User를 저장하고 영속성 컨텍스트에서 관리되는 상태로 만듦
            User savedUser = userRepository.save(user);

            // 각 사용자마다 2-3개의 잔액 생성
            for (int j = 1; j <= (i + 1); j++) {
                UserBalance balance = new UserBalance();
                // UUID는 자동 생성되므로 별도로 설정하지 않음
                balance.setUser(savedUser); // 영속성 컨텍스트에 있는 User 객체 사용
                balance.setWallet("wallet_" + i + "_" + j);
                balance.setAvailableBalance(new BigDecimal("1000.00").multiply(new BigDecimal(i * j)));
                balance.setTotalBalance(new BigDecimal("1200.00").multiply(new BigDecimal(i * j)));

                userBalanceRepository.save(balance);
            }
        }

        System.out.println("테스트 데이터 초기화 완료");
    }
}

