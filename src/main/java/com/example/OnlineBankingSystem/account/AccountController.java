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
        return "checkingAccount";
    }

    @GetMapping("/savingsAccount")
    public String savingsAccount(Model model, Principal principal){
        User user = userService.findByUserName(principal.getName());
        SavingsAccount savingsAccount = user.getSavingsAccount();

        List<SavingsAccountTransaction> savingsAccountTransactionList = transactionService.findSavingsAccountTransactionList(principal.getName());

        model.addAttribute("savingsAccount", savingsAccount);
        model.addAttribute("savingsAccountTransactionList", savingsAccountTransactionList);
        return "savingsAccount";
    }
}
