package com.example.OnlineBankingSystem.account;

import com.example.OnlineBankingSystem.user.User;
import com.example.OnlineBankingSystem.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class AccountController {

    @Autowired
    private UserService userService;

    @GetMapping("/checkingAccount")
    public String checkingAccount(Model model, Principal principal){
        User user = userService.findByUserName(principal.getName());
        CheckingAccount checkingAccount = user.getCheckingAccount();

        model.addAttribute("checkingAccount", checkingAccount);
        return "checkingAccount";
    }

    @GetMapping("/savingsAccount")
    public String savingsAccount(Model model, Principal principal){
        User user = userService.findByUserName(principal.getName());
        SavingsAccount savingsAccount = user.getSavingsAccount();

        model.addAttribute("savingsAccount", savingsAccount);
        return "savingsAccount";
    }
}
