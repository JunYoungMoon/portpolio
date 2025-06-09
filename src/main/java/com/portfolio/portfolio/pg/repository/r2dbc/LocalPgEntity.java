package com.portfolio.portfolio.pg.repository.r2dbc;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("local_pg")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocalPgEntity {
    @Id
    private Long id;
    private String tid;
    private String userId;
    private String amount;
    private String orderNo;
    private String status;
    private String message;
    private LocalDateTime createdAt;
}