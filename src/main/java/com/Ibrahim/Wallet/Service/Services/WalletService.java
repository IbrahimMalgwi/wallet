package com.Ibrahim.Wallet.Service.Services;

@Service
@Transactional
@Slf4j
public class WalletService {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final TransferRepository transferRepository;

    public WalletDTO createWallet(CreateWalletRequest request) {
        // Check if wallet already exists for user+currency
        Optional<Wallet> existingWallet = walletRepository
                .findByUserIdAndCurrency(request.getUserId(), request.getCurrency());

        if (existingWallet.isPresent()) {
            throw new BadRequestException("Wallet already exists for this user and currency");
        }

        Wallet wallet = Wallet.builder()
                .userId(request.getUserId())
                .currency(request.getCurrency())
                .balance(0L)
                .build();

        return WalletDTO.fromEntity(walletRepository.save(wallet));
    }

    public TransactionDTO creditOrDebitWallet(TransactionRequest request) {
        // Idempotency check
        if (request.getIdempotencyKey() != null) {
            Optional<Transaction> existingTransaction = transactionRepository
                    .findByIdempotencyKey(request.getIdempotencyKey());
            if (existingTransaction.isPresent()) {
                return TransactionDTO.fromEntity(existingTransaction.get());
            }
        }

        Wallet wallet = walletRepository.findById(request.getWalletId())
                .orElseThrow(() -> new NotFoundException("Wallet not found"));

        // Validate debit won't make balance negative
        if (request.getType() == TransactionType.DEBIT &&
                wallet.getBalance() < request.getAmount()) {
            throw new InsufficientFundsException("Insufficient balance");
        }

        // Update wallet balance
        Long newBalance = request.getType() == TransactionType.CREDIT
                ? wallet.getBalance() + request.getAmount()
                : wallet.getBalance() - request.getAmount();

        wallet.setBalance(newBalance);
        walletRepository.save(wallet);

        // Create transaction record
        Transaction transaction = Transaction.builder()
                .wallet(wallet)
                .amount(request.getAmount())
                .type(request.getType())
                .reference(request.getReference())
                .idempotencyKey(request.getIdempotencyKey())
                .description(request.getDescription())
                .build();

        return TransactionDTO.fromEntity(transactionRepository.save(transaction));
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public TransferDTO transferBetweenWallets(TransferRequest request) {
        // Idempotency check for transfer
        if (request.getIdempotencyKey() != null) {
            Optional<Transfer> existingTransfer = transferRepository
                    .findByIdempotencyKey(request.getIdempotencyKey());
            if (existingTransfer.isPresent()) {
                return TransferDTO.fromEntity(existingTransfer.get());
            }
        }

        // Fetch both wallets with locking
        Wallet sender = walletRepository.findByIdForUpdate(request.getSenderWalletId())
                .orElseThrow(() -> new NotFoundException("Sender wallet not found"));

        Wallet receiver = walletRepository.findByIdForUpdate(request.getReceiverWalletId())
                .orElseThrow(() -> new NotFoundException("Receiver wallet not found"));

        // Validate currencies match
        if (!sender.getCurrency().equals(receiver.getCurrency())) {
            throw new BadRequestException("Currency mismatch between wallets");
        }

        // Validate sender has sufficient funds
        if (sender.getBalance() < request.getAmount()) {
            throw new InsufficientFundsException("Insufficient balance for transfer");
        }

        // Perform atomic transfer
        sender.setBalance(sender.getBalance() - request.getAmount());
        receiver.setBalance(receiver.getBalance() + request.getAmount());

        walletRepository.save(sender);
        walletRepository.save(receiver);

        // Create transfer record
        Transfer transfer = Transfer.builder()
                .senderWallet(sender)
                .receiverWallet(receiver)
                .amount(request.getAmount())
                .reference(request.getReference())
                .idempotencyKey(request.getIdempotencyKey())
                .build();

        // Create transaction records for audit trail
        createTransferTransactions(transfer);

        return TransferDTO.fromEntity(transferRepository.save(transfer));
    }

    private void createTransferTransactions(Transfer transfer) {
        Transaction debitTx = Transaction.builder()
                .wallet(transfer.getSenderWallet())
                .amount(transfer.getAmount())
                .type(TransactionType.DEBIT)
                .reference(transfer.getReference() + "-DEBIT")
                .description("Transfer to wallet " + transfer.getReceiverWallet().getId())
                .build();

        Transaction creditTx = Transaction.builder()
                .wallet(transfer.getReceiverWallet())
                .amount(transfer.getAmount())
                .type(TransactionType.CREDIT)
                .reference(transfer.getReference() + "-CREDIT")
                .description("Transfer from wallet " + transfer.getSenderWallet().getId())
                .build();

        transactionRepository.saveAll(List.of(debitTx, creditTx));
    }

    public WalletDTO getWallet(String id) {
        Wallet wallet = walletRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Wallet not found"));
        return WalletDTO.fromEntity(wallet);
    }
}