package com.Ibrahim.Wallet.Service.controllers;

import com.Ibrahim.Wallet.Service.DTOs.CreateWalletRequest;
import com.Ibrahim.Wallet.Service.DTOs.WalletDTO;
import com.Ibrahim.Wallet.Service.Services.WalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@Validated
@Slf4j
public class WalletController {

    private final WalletService walletService;

    @PostMapping("/wallets")
    public ResponseEntity<ApiResponse<WalletDTO>> createWallet(
            @Valid @RequestBody CreateWalletRequest request) {
        WalletDTO wallet = walletService.createWallet(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Wallet created successfully", wallet));
    }

    @PostMapping("/transactions")
    public ResponseEntity<ApiResponse<TransactionDTO>> createTransaction(
            @Valid @RequestBody TransactionRequest request) {
        TransactionDTO transaction = walletService.creditOrDebitWallet(request);
        return ResponseEntity.ok(ApiResponse.success("Transaction processed", transaction));
    }

    @PostMapping("/transfers")
    public ResponseEntity<ApiResponse<TransferDTO>> transfer(
            @Valid @RequestBody TransferRequest request) {
        TransferDTO transfer = walletService.transferBetweenWallets(request);
        return ResponseEntity.ok(ApiResponse.success("Transfer completed", transfer));
    }

    @GetMapping("/wallets/{id}")
    public ResponseEntity<ApiResponse<WalletDTO>> getWallet(@PathVariable String id) {
        WalletDTO wallet = walletService.getWallet(id);
        return ResponseEntity.ok(ApiResponse.success("Wallet retrieved", wallet));
    }
}
