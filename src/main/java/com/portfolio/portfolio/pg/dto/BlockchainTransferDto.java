package com.portfolio.portfolio.pg.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlockchainTransferDto {
    private String payCode;
    private String userIdx;
    private String tradingIdx;
    private String lang;
}
