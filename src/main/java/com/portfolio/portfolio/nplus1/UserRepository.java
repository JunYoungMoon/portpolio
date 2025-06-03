package com.portfolio.portfolio.nplus1;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    // 기본 조회 (N+1 문제 발생)
    List<User> findAll();

    // 방법 1: Fetch Join 사용
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.userBalances")
    List<User> findAllWithBalances();

    // 방법 2: EntityGraph 사용 - JPQL과 함께 사용
    @EntityGraph(attributePaths = {"userBalances"})
    @Query("SELECT u FROM User u")
    List<User> findAllWithEntityGraph();
}