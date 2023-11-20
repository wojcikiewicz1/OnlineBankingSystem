package com.example.OnlineBankingSystem.transaction;

import com.example.OnlineBankingSystem.account.CheckingAccount;
import com.example.OnlineBankingSystem.account.CheckingAccountRepository;
import com.example.OnlineBankingSystem.account.SavingsAccount;
import com.example.OnlineBankingSystem.account.SavingsAccountRepository;
import com.example.OnlineBankingSystem.user.User;
import com.example.OnlineBankingSystem.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private UserService userService;

    @Autowired
    private CheckingAccountRepository checkingAccountRepository;

    @Autowired
    private SavingsAccountRepository savingsAccountRepository;

    @Autowired
    private CheckingAccountTransactionRepository checkingAccountTransactionRepository;

    @Autowired
    private SavingsAccountTransactionRepository savingsAccountTransactionRepository;

    public void deposit (String accountType, BigDecimal amount, Principal principal) {
        User user = userService.findByUserName(principal.getName());

        if (accountType.equals("Checking")) {
            CheckingAccount checkingAccount = user.getCheckingAccount();
            checkingAccount.setBalance(checkingAccount.getBalance().add(amount));
            checkingAccountRepository.save(checkingAccount);
        } else if (accountType.equals("Savings")) {
            SavingsAccount savingsAccount = user.getSavingsAccount();
            savingsAccount.setBalance(savingsAccount.getBalance().add(amount));
            savingsAccountRepository.save(savingsAccount);
        }
    }

    public void withdraw (String accountType, BigDecimal amount, Principal principal) {
        User user = userService.findByUserName(principal.getName());

        if (accountType.equals("Checking")) {
            CheckingAccount checkingAccount = user.getCheckingAccount();
            checkingAccount.setBalance(checkingAccount.getBalance().subtract(amount));
            checkingAccountRepository.save(checkingAccount);
        } else if (accountType.equals("Savings")) {
            SavingsAccount savingsAccount = user.getSavingsAccount();
            savingsAccount.setBalance(savingsAccount.getBalance().subtract(amount));
            savingsAccountRepository.save(savingsAccount);
        }
    }

    public void betweenAccountsTransfer (String transferFrom, String transferTo, String title, BigDecimal amount, CheckingAccount checkingAccount, SavingsAccount savingsAccount) throws Exception {

        if (transferFrom.equals("Checking") && transferTo.equals("Savings")) {

            checkingAccount.setBalance(checkingAccount.getBalance().subtract(amount));
            savingsAccount.setBalance(savingsAccount.getBalance().add(amount));
            checkingAccountRepository.save(checkingAccount);
            savingsAccountRepository.save(savingsAccount);

            CheckingAccountTransaction checkingAccountTransaction = new CheckingAccountTransaction();
            checkingAccountTransaction.setCheckingAccount(checkingAccount);
            checkingAccountTransaction.setTransferFrom(transferFrom);
            checkingAccountTransaction.setTransferTo(transferTo);
            checkingAccountTransaction.setTitle(title);
            checkingAccountTransaction.setAmount(amount);
            checkingAccountTransaction.setAvailableBalance(checkingAccount.getBalance());
            checkingAccountTransactionRepository.save(checkingAccountTransaction);

            SavingsAccountTransaction savingsAccountTransaction = new SavingsAccountTransaction();
            savingsAccountTransaction.setSavingsAccount(savingsAccount);
            savingsAccountTransaction.setTransferFrom(transferFrom);
            savingsAccountTransaction.setTransferTo(transferTo);
            savingsAccountTransaction.setTitle(title);
            savingsAccountTransaction.setAmount(amount);
            savingsAccountTransaction.setAvailableBalance(savingsAccount.getBalance());
            savingsAccountTransactionRepository.save(savingsAccountTransaction);

        }else if (transferFrom.equals("Savings") && transferTo.equals("Checking")){

            savingsAccount.setBalance(savingsAccount.getBalance().subtract(amount));
            checkingAccount.setBalance(checkingAccount.getBalance().add(amount));
            savingsAccountRepository.save(savingsAccount);
            checkingAccountRepository.save(checkingAccount);

            SavingsAccountTransaction savingsAccountTransaction = new SavingsAccountTransaction();
            savingsAccountTransaction.setSavingsAccount(savingsAccount);
            savingsAccountTransaction.setTransferFrom(transferFrom);
            savingsAccountTransaction.setTransferTo(transferTo);
            savingsAccountTransaction.setTitle(title);
            savingsAccountTransaction.setAmount(amount);
            savingsAccountTransaction.setAvailableBalance(savingsAccount.getBalance());
            savingsAccountTransactionRepository.save(savingsAccountTransaction);

            CheckingAccountTransaction checkingAccountTransaction = new CheckingAccountTransaction();
            checkingAccountTransaction.setCheckingAccount(checkingAccount);
            checkingAccountTransaction.setTransferFrom(transferFrom);
            checkingAccountTransaction.setTransferTo(transferTo);
            checkingAccountTransaction.setTitle(title);
            checkingAccountTransaction.setAmount(amount);
            checkingAccountTransaction.setAvailableBalance(checkingAccount.getBalance());
            checkingAccountTransactionRepository.save(checkingAccountTransaction);
        } else
            throw new Exception("An error occured!");
    }

    public List<CheckingAccountTransaction> findCheckingAccountTransactionList(String username) {
        User user = userService.findByUserName(username);
        List<CheckingAccountTransaction> checkingAccountTransactionList = user.getCheckingAccount().getCheckingAccountTransactionList();
        return checkingAccountTransactionList;
    }

    public List<SavingsAccountTransaction> findSavingsAccountTransactionList(String username) {
        User user = userService.findByUserName(username);
        List<SavingsAccountTransaction> savingsAccountTransactionList = user.getSavingsAccount().getSavingsAccountTransactionList();
        return savingsAccountTransactionList;
    }
}