package com.portfolio.portfolio.nplus1.repository.jpa;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "p_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "bank_account_number", nullable = false)
    private String bankAccountNumber;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    // N+1 문제를 확인하기 위한 OneToMany 관계 설정
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<UserBalance> userBalances = new ArrayList<>();

    public enum UserRole {
        HUB_MANAGER, MASTER
    }
}
