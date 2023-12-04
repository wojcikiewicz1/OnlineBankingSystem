package com.example.OnlineBankingSystem.transaction;

import com.example.OnlineBankingSystem.account.CheckingAccount;
import com.example.OnlineBankingSystem.account.CheckingAccountRepository;
import com.example.OnlineBankingSystem.account.SavingsAccount;
import com.example.OnlineBankingSystem.account.SavingsAccountRepository;
import com.example.OnlineBankingSystem.recipient.Recipient;
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

        if (accountType.equals("Checking Account")) {
            CheckingAccount checkingAccount = user.getCheckingAccount();
            checkingAccount.setBalance(checkingAccount.getBalance().add(amount));
            checkingAccountRepository.save(checkingAccount);

            CheckingAccountTransaction checkingAccountTransaction = new CheckingAccountTransaction(
                    checkingAccount, "-", "Checking Account", "deposit", amount);
            checkingAccountTransactionRepository.save(checkingAccountTransaction);

        } else if (accountType.equals("Savings Account")) {
            SavingsAccount savingsAccount = user.getSavingsAccount();
            savingsAccount.setBalance(savingsAccount.getBalance().add(amount));
            savingsAccountRepository.save(savingsAccount);

            SavingsAccountTransaction savingsAccountTransaction = new SavingsAccountTransaction(
                    savingsAccount, "-", "Savings Account", "deposit", amount);
            savingsAccountTransactionRepository.save(savingsAccountTransaction);
        }
    }

    public void withdraw (String accountType, BigDecimal amount, Principal principal) {
        User user = userService.findByUserName(principal.getName());

        if (accountType.equals("Checking Account")) {
            CheckingAccount checkingAccount = user.getCheckingAccount();
            checkingAccount.setBalance(checkingAccount.getBalance().subtract(amount));
            checkingAccountRepository.save(checkingAccount);

            CheckingAccountTransaction checkingAccountTransaction = new CheckingAccountTransaction(
                    checkingAccount, "Checking Account", "-", "withdraw", amount);
            checkingAccountTransactionRepository.save(checkingAccountTransaction);

        } else if (accountType.equals("Savings Account")) {
            SavingsAccount savingsAccount = user.getSavingsAccount();
            savingsAccount.setBalance(savingsAccount.getBalance().subtract(amount));
            savingsAccountRepository.save(savingsAccount);

            SavingsAccountTransaction savingsAccountTransaction = new SavingsAccountTransaction(
                    savingsAccount, "Savings Account", "-", "withdraw", amount);
            savingsAccountTransactionRepository.save(savingsAccountTransaction);
        }
    }

    public void betweenAccountsTransfer (String transferFrom, String transferTo, String title, BigDecimal amount, CheckingAccount checkingAccount, SavingsAccount savingsAccount) throws Exception {

        if (transferFrom.equals("Checking Account") && transferTo.equals("Savings Account")) {

            checkingAccount.setBalance(checkingAccount.getBalance().subtract(amount));
            savingsAccount.setBalance(savingsAccount.getBalance().add(amount));
            checkingAccountRepository.save(checkingAccount);
            savingsAccountRepository.save(savingsAccount);

            CheckingAccountTransaction checkingAccountTransaction = new CheckingAccountTransaction(
                    checkingAccount, transferFrom, transferTo, title, amount);
            checkingAccountTransactionRepository.save(checkingAccountTransaction);

            SavingsAccountTransaction savingsAccountTransaction = new SavingsAccountTransaction(
                    savingsAccount, transferFrom, transferTo, title, amount);
            savingsAccountTransactionRepository.save(savingsAccountTransaction);

        } else if (transferFrom.equals("Savings Account") && transferTo.equals("Checking Account")){

            savingsAccount.setBalance(savingsAccount.getBalance().subtract(amount));
            checkingAccount.setBalance(checkingAccount.getBalance().add(amount));
            savingsAccountRepository.save(savingsAccount);
            checkingAccountRepository.save(checkingAccount);

            SavingsAccountTransaction savingsAccountTransaction = new SavingsAccountTransaction(
                    savingsAccount, transferFrom, transferTo, title, amount);
            savingsAccountTransactionRepository.save(savingsAccountTransaction);

            CheckingAccountTransaction checkingAccountTransaction = new CheckingAccountTransaction(
                    checkingAccount, transferFrom, transferTo, title, amount);
            checkingAccountTransactionRepository.save(checkingAccountTransaction);
        } else
            throw new Exception();
    }

    public void regularTransfer (String transferFrom, Recipient recipient, String title, BigDecimal amount, CheckingAccount checkingAccount, SavingsAccount savingsAccount) throws Exception {

        if (transferFrom.equals("Checking Account")) {

            checkingAccount.setBalance(checkingAccount.getBalance().subtract(amount));
            checkingAccountRepository.save(checkingAccount);

            CheckingAccountTransaction checkingAccountTransaction = new CheckingAccountTransaction(
                    checkingAccount, transferFrom, recipient.getName(), title, amount);
            checkingAccountTransactionRepository.save(checkingAccountTransaction);

            CheckingAccount recipientCheckingAccount = checkingAccountRepository.findByAccountNumber(recipient.getAccountNumber());
            SavingsAccount recipientSavingsAccount = savingsAccountRepository.findByAccountNumber(recipient.getAccountNumber());

            if (recipientCheckingAccount != null && recipient.getAccountNumber() == recipientCheckingAccount.getAccountNumber()) {
                recipientCheckingAccount.setBalance(recipientCheckingAccount.getBalance().add(amount));
                checkingAccountRepository.save(recipientCheckingAccount);

                CheckingAccountTransaction recipientCheckingTransaction = new CheckingAccountTransaction(
                        recipientCheckingAccount, recipient.getUser().getFirstName() + " " + recipient.getUser().getLastName(), "Checking Account", title, amount);
                recipientCheckingAccount.getCheckingAccountTransactionList().add(recipientCheckingTransaction);
                checkingAccountTransactionRepository.save(recipientCheckingTransaction);

            } else if (recipientSavingsAccount != null && recipient.getAccountNumber() == recipientSavingsAccount.getAccountNumber()) {
                recipientSavingsAccount.setBalance(recipientSavingsAccount.getBalance().add(amount));
                savingsAccountRepository.save(recipientSavingsAccount);

                SavingsAccountTransaction recipientSavingsTransaction = new SavingsAccountTransaction(
                        recipientSavingsAccount, recipient.getUser().getFirstName() + " " + recipient.getUser().getLastName(), "Savings Account", title, amount);
                recipientSavingsAccount.getSavingsAccountTransactionList().add(recipientSavingsTransaction);
                savingsAccountTransactionRepository.save(recipientSavingsTransaction);
            }

        } else if (transferFrom.equals("Savings Account")) {
            savingsAccount.setBalance(savingsAccount.getBalance().subtract(amount));
            savingsAccountRepository.save(savingsAccount);

            SavingsAccountTransaction savingsAccountTransaction = new SavingsAccountTransaction(
                    savingsAccount, transferFrom, recipient.getName(), title, amount);
            savingsAccountTransactionRepository.save(savingsAccountTransaction);

            CheckingAccount recipientCheckingAccount = checkingAccountRepository.findByAccountNumber(recipient.getAccountNumber());
            SavingsAccount recipientSavingsAccount = savingsAccountRepository.findByAccountNumber(recipient.getAccountNumber());

            if (recipientCheckingAccount != null && recipient.getAccountNumber() == recipientCheckingAccount.getAccountNumber()) {
                recipientCheckingAccount.setBalance(recipientCheckingAccount.getBalance().add(amount));
                checkingAccountRepository.save(recipientCheckingAccount);

                CheckingAccountTransaction recipientCheckingTransaction = new CheckingAccountTransaction(
                        recipientCheckingAccount, recipient.getUser().getFirstName() + " " + recipient.getUser().getLastName(), "Checking Account", title, amount);
                recipientCheckingAccount.getCheckingAccountTransactionList().add(recipientCheckingTransaction);
                checkingAccountTransactionRepository.save(recipientCheckingTransaction);

            } else if (recipientSavingsAccount != null &&recipient.getAccountNumber() == recipientSavingsAccount.getAccountNumber()) {
                recipientSavingsAccount.setBalance(recipientSavingsAccount.getBalance().add(amount));
                savingsAccountRepository.save(recipientSavingsAccount);

                SavingsAccountTransaction recipientSavingsTransaction = new SavingsAccountTransaction(
                        recipientSavingsAccount, recipient.getUser().getFirstName() + " " + recipient.getUser().getLastName(), "Savings Account", title, amount);
                recipientSavingsAccount.getSavingsAccountTransactionList().add(recipientSavingsTransaction);
                savingsAccountTransactionRepository.save(recipientSavingsTransaction);
            }
        } else {
            throw new Exception();
        }
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
