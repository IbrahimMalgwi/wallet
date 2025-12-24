package com.Ibrahim.Wallet.Service.Repositories;

import com.Ibrahim.Wallet.Service.Entities.Transaction;
import com.Ibrahim.Wallet.Service.Entities.Wallet;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, String> {
    Optional<Wallet> findByUserIdAndCurrency(String userId, String currency);

    @Query("SELECT w FROM Wallet w WHERE w.id = :id")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Wallet> findByIdForUpdate(@Param("id") String id);
}

