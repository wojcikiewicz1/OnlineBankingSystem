package com.example.OnlineBankingSystem.recipient;


import com.example.OnlineBankingSystem.user.User;
import com.example.OnlineBankingSystem.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class RecipientController {

    @Autowired
    private RecipientService recipientService;

    @Autowired
    private UserService userService;

    @GetMapping("/recipient")
    public String showRecipientForm(Model model, Principal principal) {
        List<Recipient> recipientList = recipientService.findRecipientList(principal);

        Recipient recipient = new Recipient();

        model.addAttribute("recipient", recipient);
        model.addAttribute("recipientList", recipientList);

        return "recipient";
    }

    @PostMapping("/recipient/save")
    public String saveRecipientForm(@ModelAttribute("recipient") Recipient recipient, Principal principal) {

        User existingUser = userService.findByUserName(principal.getName());
        recipient.setUser(existingUser);
        recipientService.saveRecipient(recipient);

        return "redirect:/recipient";
    }

    @GetMapping("/recipient/delete")
    @Transactional
    public String deleteRecipientForm(@RequestParam(value = "recipientName") String recipientName, Model model, Principal principal){
        List<Recipient> recipientList = recipientService.findRecipientList(principal);

        recipientService.deleteRecipientByName(recipientName);

        Recipient recipient = new Recipient();
        model.addAttribute("recipientList", recipientList);
        model.addAttribute("recipient", recipient);

        return "redirect:/recipient";
    }
}
