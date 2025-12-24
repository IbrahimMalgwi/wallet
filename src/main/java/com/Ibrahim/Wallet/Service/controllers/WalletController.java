package com.Ibrahim.Wallet.Service.controllers;

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
