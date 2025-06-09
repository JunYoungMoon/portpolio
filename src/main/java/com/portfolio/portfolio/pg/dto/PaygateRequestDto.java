package com.portfolio.portfolio.pg.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaygateRequestDto {
    private String pgcode;
    private String clientId;
    private String userId;
    private String orderNo;
    private String amount;
    private String productName;
    private String customParameter;
    private String returnUrl;
    private String callbackUrl;
    private String cancelUrl;
}
