package com.portfolio.portfolio.ddd.infrastructure;

import com.portfolio.portfolio.ddd.domain.model.entity.DDDUser;
import com.portfolio.portfolio.ddd.domain.repository.UserRepository;
import com.portfolio.portfolio.ddd.infrastructure.repository.JpaUserRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("userRepositoryImpl")
public class JpaUserRepositoryImpl implements UserRepository {

    private final JpaUserRepository jpaRepository;

    public JpaUserRepositoryImpl(JpaUserRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public DDDUser findById(Long userId) {
        return jpaRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다: " + userId));
    }

    @Override
    public void save(DDDUser DDDUser) {
        jpaRepository.save(DDDUser);
    }

    @Override
    public List<DDDUser> findByEmail(String email) {
        return jpaRepository.findByEmail(email);
    }
}