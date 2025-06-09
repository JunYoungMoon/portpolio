package com.portfolio.portfolio.pg;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
class BlockchainResult {
    private boolean success;
    private String message;

    public static BlockchainResult success() {
        return BlockchainResult.builder().success(true).build();
    }

    public static BlockchainResult failed(String message) {
        return BlockchainResult.builder().success(false).message(message).build();
    }
}