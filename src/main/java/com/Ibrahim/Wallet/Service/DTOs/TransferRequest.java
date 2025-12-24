package com.Ibrahim.Wallet.Service.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class TransferRequest {
    @NotBlank(message = "Sender wallet ID is required")
    private String senderWalletId;

    @NotBlank(message = "Receiver wallet ID is required")
    private String receiverWalletId;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private Long amount;

    @NotBlank(message = "Reference is required")
    private String reference;

    private String idempotencyKey;
}
