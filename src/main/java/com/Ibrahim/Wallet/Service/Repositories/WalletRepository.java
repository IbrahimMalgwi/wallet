package com.Ibrahim.Wallet.Service.Repositories;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, String> {
    Optional<Wallet> findByUserIdAndCurrency(String userId, String currency);

    @Query("SELECT w FROM Wallet w WHERE w.id = :id")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Wallet> findByIdForUpdate(@Param("id") String id);
}

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
    Optional<Transaction> findByIdempotencyKey(String idempotencyKey);
    Optional<Transaction> findByReference(String reference);
}