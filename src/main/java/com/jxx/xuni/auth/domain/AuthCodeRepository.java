package com.jxx.xuni.auth.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthCodeRepository extends JpaRepository<AuthCode, String> {
    Optional<AuthCode> findByEmailAndUsageType(String email, UsageType usageType);
}