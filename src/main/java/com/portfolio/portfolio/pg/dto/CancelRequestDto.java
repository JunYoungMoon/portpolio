package com.portfolio.portfolio.pg.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CancelRequestDto {
    private String pgcode;
    private String clientId;
    private String userId;
    private String tid;
    private String amount;
    private String ipAddr;
}
