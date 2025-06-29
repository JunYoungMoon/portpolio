package com.portfolio.portfolio.nplus1;

import com.portfolio.portfolio.nplus1.repository.jpa.Nplus1User;
import com.portfolio.portfolio.nplus1.repository.jpa.Nplus1UserBalance;
import com.portfolio.portfolio.nplus1.repository.jpa.Nplus1UserBalanceRepository;
import com.portfolio.portfolio.nplus1.repository.jpa.Nplus1UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

// 6. 테스트 데이터 생성을 위한 초기화 클래스
@Component
public class DataInitializer {

    private final Nplus1UserRepository nplus1UserRepository;
    private final Nplus1UserBalanceRepository nplus1UserBalanceRepository;

    public DataInitializer(Nplus1UserRepository nplus1UserRepository, Nplus1UserBalanceRepository nplus1UserBalanceRepository) {
        this.nplus1UserRepository = nplus1UserRepository;
        this.nplus1UserBalanceRepository = nplus1UserBalanceRepository;
    }

    @PostConstruct
    @Transactional(transactionManager = "jpaTransactionManager")
    public void initializeData() {
        // 기존 데이터가 있으면 초기화하지 않음
        if (nplus1UserRepository.count() > 0) {
            return;
        }

        // 테스트용 사용자 생성
        for (int i = 1; i <= 3; i++) {
            Nplus1User nplus1User = new Nplus1User();
            // UUID는 자동 생성되므로 별도로 설정하지 않음
            nplus1User.setUsername("user" + i);
            nplus1User.setEmail("user" + i + "@example.com");
            nplus1User.setPassword("password" + i);
            nplus1User.setPhone("010-1234-567" + i);
            nplus1User.setBankAccountNumber("1234-5678-90" + i);
            nplus1User.setRole(i == 1 ? Nplus1User.UserRole.MASTER : Nplus1User.UserRole.HUB_MANAGER);

            // 먼저 User를 저장하고 영속성 컨텍스트에서 관리되는 상태로 만듦
            Nplus1User savedNplus1User = nplus1UserRepository.save(nplus1User);

            // 각 사용자마다 2-3개의 잔액 생성
            for (int j = 1; j <= (i + 1); j++) {
                Nplus1UserBalance balance = new Nplus1UserBalance();
                // UUID는 자동 생성되므로 별도로 설정하지 않음
                balance.setNplus1User(savedNplus1User); // 영속성 컨텍스트에 있는 User 객체 사용
                balance.setWallet("wallet_" + i + "_" + j);
                balance.setAvailableBalance(new BigDecimal("1000.00").multiply(new BigDecimal(i * j)));
                balance.setTotalBalance(new BigDecimal("1200.00").multiply(new BigDecimal(i * j)));

                nplus1UserBalanceRepository.save(balance);
            }
        }

        System.out.println("테스트 데이터 초기화 완료");
    }
}

