package com.portfolio.portfolio.ai.application;

public record UserRegistrationRequest(
        String username,
        String email,
        String password,
        String name
) {}