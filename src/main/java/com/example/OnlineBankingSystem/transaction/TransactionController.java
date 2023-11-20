package com.example.OnlineBankingSystem.transaction;

import com.example.OnlineBankingSystem.account.CheckingAccount;
import com.example.OnlineBankingSystem.account.SavingsAccount;
import com.example.OnlineBankingSystem.user.User;
import com.example.OnlineBankingSystem.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;

@Controller
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;

    @GetMapping("/deposit")
    public String showDepositForm (Model model) {
        model.addAttribute("accountType", "");
        model.addAttribute("amount", "");
        return "deposit";
    }

    @PostMapping("/deposit/save")
    public String deposit (@ModelAttribute("accountType") String accountType, @ModelAttribute("amount") BigDecimal amount, Principal principal, BindingResult result) {

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            result.hasErrors();
            return "redirect:/deposit?error";
        }

        if (accountType.isEmpty()) {
            result.hasErrors();
            return "redirect:/withdraw?error1";
        }

        //??????????????????????????????????????
        if (amount == null) {
            result.hasErrors();
            return "redirect:/withdraw?error3";
        }

        transactionService.deposit(accountType, amount, principal);
        return "redirect:/successoperation";
    }

    @GetMapping ("/withdraw")
    public String showWithdrawForm(Model model) {
        model.addAttribute("accountType", "");
        model.addAttribute("amount", "");
        return "withdraw";
    }

    @PostMapping ("/withdraw/save")
    public String withdraw (@ModelAttribute("accountType") String accountType, @ModelAttribute("amount")  BigDecimal amount, Principal principal, BindingResult result) {

        User user = userService.findByUserName(principal.getName());
        CheckingAccount checkingAccount = user.getCheckingAccount();
        SavingsAccount savingsAccount = user.getSavingsAccount();

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            result.hasErrors();
            return "redirect:/withdraw?error";
        }

        if (accountType.isEmpty()) {
            result.hasErrors();
            return "redirect:/withdraw?error1";
        }

        if ((accountType.equals("Checking") && checkingAccount.getBalance().compareTo(amount) < 0) ||
                (accountType.equals("Savings") && savingsAccount.getBalance().compareTo(amount) < 0)) {
            result.hasErrors();
            return "redirect:/withdraw?error2";
        }

        //??????????????????????????????????????
        if (amount == null) {
            result.hasErrors();
            return "redirect:/withdraw?error3";
        }

        transactionService.withdraw(accountType, amount, principal);
        return "redirect:/successoperation";
    }

    @GetMapping ("/betweenAccountsTransfer")
    public String showbetweenAccountsTransferForm(Model model) {
        model.addAttribute("transferFrom", "");
        model.addAttribute("TransferTo", "");
        model.addAttribute("title", "");
        model.addAttribute("amount", "");

        return "betweenAccountsTransfer";
    }

    @PostMapping("/betweenAccountsTransfer/save")
    public String showBetweenAccountsTransferForm(@ModelAttribute("transferFrom") String transferFrom,
                                                  @ModelAttribute("transferTo") String transferTo,
                                                  @ModelAttribute("title") String title,
                                                  @ModelAttribute("amount")BigDecimal amount,
                                                  Principal principal, BindingResult result) throws Exception {

        User user = userService.findByUserName(principal.getName());
        CheckingAccount checkingAccount = user.getCheckingAccount();
        SavingsAccount savingsAccount = user.getSavingsAccount();

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            result.hasErrors();
            return "redirect:/betweenAccountsTransfer?error";
        }

        if (transferFrom.isEmpty() || transferTo.isEmpty()) {
            result.hasErrors();
            return "redirect:/withdraw?error1";
        }

        if ((transferFrom.equals("Checking") && checkingAccount.getBalance().compareTo(amount) < 0) ||
                ((transferFrom.equals("Savings") && savingsAccount.getBalance().compareTo(amount) < 0))) {
            result.hasErrors();
            return "redirect:/betweenAccountsTransfer?error2";
        }

        transactionService.betweenAccountsTransfer(transferFrom, transferTo, title, amount, checkingAccount, savingsAccount);

        return "redirect:/successoperation";
    }

}
