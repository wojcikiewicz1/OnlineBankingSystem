package com.example.OnlineBankingSystem.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavingsAccountTransactionRepository extends JpaRepository<SavingsAccountTransaction, Long> {
}
