package com.portfolio.portfolio.ddd.domain.repository;

import com.portfolio.portfolio.ddd.domain.model.entity.DDDUser;

import java.util.List;

public interface UserRepository {
    DDDUser findById(Long userId);
    void save(DDDUser DDDUser);
    List<DDDUser> findByEmail(String email);
}
