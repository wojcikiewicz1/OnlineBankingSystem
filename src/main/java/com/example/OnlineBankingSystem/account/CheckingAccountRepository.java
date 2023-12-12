package com.example.OnlineBankingSystem.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckingAccountRepository extends JpaRepository<CheckingAccount,Long> {

    CheckingAccount findByAccountNumber(int accountNumber);
}
