package com.Ibrahim.Wallet.Service.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

// CreateWalletRequest.java
@Data
public class CreateWalletRequest {
    @NotBlank(message = "User ID is required")
    private String userId;

    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be 3 uppercase letters")
    private String currency = "NGN";
}
