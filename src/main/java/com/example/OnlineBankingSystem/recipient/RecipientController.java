package com.example.OnlineBankingSystem.recipient;


import com.example.OnlineBankingSystem.account.CheckingAccountRepository;
import com.example.OnlineBankingSystem.account.SavingsAccountRepository;
import com.example.OnlineBankingSystem.user.User;
import com.example.OnlineBankingSystem.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class RecipientController {

    @Autowired
    private RecipientService recipientService;

    @Autowired
    private UserService userService;

    @Autowired
    private CheckingAccountRepository checkingAccountRepository;

    @Autowired
    private SavingsAccountRepository savingsAccountRepository;

    @GetMapping("/recipient")
    public String showRecipientForm(Model model, Principal principal) {
        List<Recipient> recipientList = recipientService.findRecipientList(principal);

        Recipient recipient = new Recipient();

        model.addAttribute("recipient", recipient);
        model.addAttribute("recipientList", recipientList);

        return "recipient";
    }

    @PostMapping("/recipient/save")
    public String saveRecipientForm(@ModelAttribute("recipient") Recipient recipient, Principal principal, BindingResult result) {

        User user = userService.findByUserName(principal.getName());
        recipient.setUser(user);

        if ((checkingAccountRepository.findByAccountNumber(recipient.getAccountNumber()) != null && savingsAccountRepository.findByAccountNumber(recipient.getAccountNumber()) == null) ||
                (checkingAccountRepository.findByAccountNumber(recipient.getAccountNumber()) == null && savingsAccountRepository.findByAccountNumber(recipient.getAccountNumber()) != null)){
        } else {
            result.hasErrors();
            return "redirect:/recipient?error";
        }

        if (recipient.getAccountNumber() == user.getSavingsAccount().getAccountNumber() || recipient.getAccountNumber() == user.getCheckingAccount().getAccountNumber()) {
            result.hasErrors();
            return "redirect:/recipient?error1";
        }

        recipientService.saveRecipient(recipient);

        return "redirect:/recipient";
    }

    @GetMapping("/recipient/delete")
    @Transactional
    public String deleteRecipientForm(@RequestParam(value = "recipientName") String recipientName, Model model, Principal principal){
        List<Recipient> recipientList = recipientService.findRecipientList(principal);

        Recipient recipient = new Recipient();

        model.addAttribute("recipientList", recipientList);
        model.addAttribute("recipient", recipient);

        recipientService.deleteRecipientByName(recipientName);

        return "redirect:/recipient";
    }
}
