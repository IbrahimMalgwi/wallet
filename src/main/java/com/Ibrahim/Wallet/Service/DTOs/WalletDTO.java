package com.Ibrahim.Wallet.Service.DTOs;

// WalletDTO.java
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

// CreateWalletRequest.java
@Data
public class CreateWalletRequest {
    @NotBlank(message = "User ID is required")
    private String userId;

    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be 3 uppercase letters")
    private String currency = "NGN";
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

// TransferRequest.java
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