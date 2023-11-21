package com.example.OnlineBankingSystem.recipient;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipientRepository extends JpaRepository<Recipient, Long> {

    Recipient findByName(String name);

    List<Recipient> findAll();

    void deleteByName(String recipientName);

}
