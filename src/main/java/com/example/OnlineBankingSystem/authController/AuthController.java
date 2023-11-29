package com.example.OnlineBankingSystem.authController;

import com.example.OnlineBankingSystem.account.CheckingAccount;
import com.example.OnlineBankingSystem.account.SavingsAccount;
import com.example.OnlineBankingSystem.transaction.CheckingAccountTransaction;
import com.example.OnlineBankingSystem.transaction.SavingsAccountTransaction;
import com.example.OnlineBankingSystem.transaction.TransactionService;
import com.example.OnlineBankingSystem.user.User;
import com.example.OnlineBankingSystem.user.UserRepository;
import com.example.OnlineBankingSystem.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/index")
    public String home() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping ("/register")
    public String showRegistrationForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/register/save")
    public String registration(@ModelAttribute("user")User user, Model model, BindingResult result) {

        User existingUser = userRepository.findByUsername(user.getUsername());
        User existingUser1 = userRepository.findByEmail(user.getEmail());

        if(existingUser != null && existingUser.getUsername() != null && !existingUser.getUsername().isEmpty()){
            result.rejectValue("username", null, "There is already an account registered with the same username");
        }

        if(existingUser1 != null && existingUser1.getEmail() != null && !existingUser1.getEmail().isEmpty()){
            result.rejectValue("email", null, "There is already an account registered with the same email");
        }

        if(result.hasErrors()){
            model.addAttribute("user", user);
            return "/register";
        }

        userService.saveUser(user);
        return "redirect:/register?success";
    }

    @GetMapping("/menu")
    public String menu(Model model, Principal principal) {
        User user = userService.findByUserName(principal.getName());

        CheckingAccount checkingAccount = user.getCheckingAccount();
        SavingsAccount savingsAccount = user.getSavingsAccount();

        List<CheckingAccountTransaction> checkingAccountTransactionList = transactionService.findCheckingAccountTransactionList(principal.getName());
        List<SavingsAccountTransaction> savingsAccountTransactionList = transactionService.findSavingsAccountTransactionList(principal.getName());

        model.addAttribute("checkingAccount", checkingAccount);
        model.addAttribute("checkingAccountTransactionList", checkingAccountTransactionList);
        model.addAttribute("savingsAccount", savingsAccount);
        model.addAttribute("savingsAccountTransactionList", savingsAccountTransactionList);

        Comparator<CheckingAccountTransaction> comparator = (t1, t2) -> Integer.valueOf((int) t2.getOperationDate().getTime()).compareTo((int) t1.getOperationDate().getTime());
        Collections.sort(checkingAccountTransactionList, comparator);
        Comparator<SavingsAccountTransaction> comparator2 = (t1, t2) -> Integer.valueOf((int) t2.getOperationDate().getTime()).compareTo((int) t1.getOperationDate().getTime());
        Collections.sort(savingsAccountTransactionList, comparator2);
        return "menu";
    }

    @GetMapping ("/logout")
    public String logout() {
        return "logout";
    }
}
