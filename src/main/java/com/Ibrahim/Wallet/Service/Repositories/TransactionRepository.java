package com.Ibrahim.Wallet.Service.Repositories;

import com.Ibrahim.Wallet.Service.Entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
    Optional<Transaction> findByIdempotencyKey(String idempotencyKey);
    Optional<Transaction> findByReference(String reference);
}
