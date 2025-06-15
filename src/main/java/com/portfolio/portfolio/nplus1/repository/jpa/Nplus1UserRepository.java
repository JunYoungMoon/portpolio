package com.portfolio.portfolio.nplus1.repository.jpa;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface Nplus1UserRepository extends JpaRepository<Nplus1User, UUID> {

    // 기본 조회 (N+1 문제 발생)
    List<Nplus1User> findAll();

    // 방법 1: Fetch Join 사용
    @Query("SELECT u FROM Nplus1User u LEFT JOIN FETCH u.nplus1UserBalances")
    List<Nplus1User> findAllWithBalances();

    // 방법 2: EntityGraph 사용 - JPQL과 함께 사용
    @EntityGraph(attributePaths = {"nplus1UserBalances"})
    @Query("SELECT u FROM Nplus1User u")
    List<Nplus1User> findAllWithEntityGraph();
}