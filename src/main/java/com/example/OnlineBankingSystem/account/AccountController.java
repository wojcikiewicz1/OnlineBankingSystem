package com.example.OnlineBankingSystem.account;

import com.example.OnlineBankingSystem.transaction.CheckingAccountTransaction;
import com.example.OnlineBankingSystem.transaction.SavingsAccountTransaction;
import com.example.OnlineBankingSystem.transaction.TransactionService;
import com.example.OnlineBankingSystem.user.User;
import com.example.OnlineBankingSystem.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
public class AccountController {

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/checkingAccount")
    public String checkingAccount(Model model, Principal principal){
        User user = userService.findByUserName(principal.getName());
        CheckingAccount checkingAccount = user.getCheckingAccount();

        List<CheckingAccountTransaction> checkingAccountTransactionList = transactionService.findCheckingAccountTransactionList(principal.getName());

        model.addAttribute("checkingAccount", checkingAccount);
        model.addAttribute("checkingAccountTransactionList", checkingAccountTransactionList);

        Comparator<CheckingAccountTransaction> comparator = (t1, t2) -> Integer.valueOf((int) t2.getOperationDate().getTime()).compareTo((int) t1.getOperationDate().getTime());
        Collections.sort(checkingAccountTransactionList, comparator);

        return "checkingAccount";
    }

    @GetMapping("/savingsAccount")
    public String savingsAccount(Model model, Principal principal){
        User user = userService.findByUserName(principal.getName());
        SavingsAccount savingsAccount = user.getSavingsAccount();

        List<SavingsAccountTransaction> savingsAccountTransactionList = transactionService.findSavingsAccountTransactionList(principal.getName());

        model.addAttribute("savingsAccount", savingsAccount);
        model.addAttribute("savingsAccountTransactionList", savingsAccountTransactionList);

        Comparator<SavingsAccountTransaction> comparator = (t1, t2) -> Integer.valueOf((int) t2.getOperationDate().getTime()).compareTo((int) t1.getOperationDate().getTime());
        Collections.sort(savingsAccountTransactionList, comparator);

        return "savingsAccount";
    }
}
