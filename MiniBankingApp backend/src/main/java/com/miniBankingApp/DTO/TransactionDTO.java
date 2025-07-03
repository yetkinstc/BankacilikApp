package com.miniBankingApp.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import java.util.UUID;

@Data
public class TransactionDTO {
    private Long id;
    private UUID fromAccountId;
    private UUID toAccountId;
    private BigDecimal amount;
    private LocalDateTime transactionDate;
    private String status;
}
