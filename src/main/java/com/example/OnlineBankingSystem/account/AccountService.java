package com.example.OnlineBankingSystem.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Random;

@Service
public class AccountService {

    @Autowired
    private CheckingAccountRepository checkingAccountRepository;

    @Autowired
    private SavingsAccountRepository savingsAccountRepository;

    Random random = new Random();

    public CheckingAccount createCheckingAccount() {
        CheckingAccount checkingAccount = new CheckingAccount();
        checkingAccount.setAccountNumber(random.nextInt(9998 + 1 - 1000) + 1000);
        checkingAccount.setBalance(new BigDecimal(0.00));

        checkingAccountRepository.save(checkingAccount);

        return checkingAccountRepository.findByAccountNumber(checkingAccount.getAccountNumber());
    }

    public SavingsAccount createSavingsAccount() {
        SavingsAccount SavingsAccount = new SavingsAccount();
        SavingsAccount.setAccountNumber(random.nextInt(9998 + 1 - 1000) + 1000);
        SavingsAccount.setBalance(new BigDecimal(0.00));

        savingsAccountRepository.save(SavingsAccount);

        return savingsAccountRepository.findByAccountNumber(SavingsAccount.getAccountNumber());
    }
}
