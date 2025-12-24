package com.Ibrahim.Wallet.Service.DTOs;

import com.Ibrahim.Wallet.Service.Entities.Wallet;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletDTO {
    private String id;
    private String userId;
    private Long balance;
    private String currency;
    private LocalDateTime createdAt;

    public static WalletDTO fromEntity(Wallet wallet) {
        return new WalletDTO(
                wallet.getId(),
                wallet.getUserId(),
                wallet.getBalance(),
                wallet.getCurrency(),
                wallet.getCreatedAt()
        );
    }
}

// TransactionRequest.java
@Data
public class TransactionRequest {
    @NotBlank(message = "Wallet ID is required")
    private String walletId;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private Long amount; // In minor units

    @NotBlank(message = "Reference is required")
    private String reference;

    private String idempotencyKey;

    private String description;

    @NotNull(message = "Transaction type is required")
    private TransactionType type;
}


