package com.example.OnlineBankingSystem.recipient;

import com.example.OnlineBankingSystem.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipientService {

    @Autowired
    private RecipientRepository recipientRepository;

    public Recipient findRecipientByName(String recipientName) {

        return recipientRepository.findByName(recipientName);
    }

    public List<Recipient> findRecipientList(Principal principal) {
        String username = principal.getName();
        List<Recipient> recipientList = recipientRepository.findAll().stream()
                .filter(recipient -> username.equals(recipient.getUser().getUsername()))
                .collect(Collectors.toList());

        return recipientList;
    }

    public Recipient saveRecipient(Recipient recipient) {

        return recipientRepository.save(recipient);
    }

    public void deleteRecipientByName(String recipientName) {

        recipientRepository.deleteByName(recipientName);
    }


}
