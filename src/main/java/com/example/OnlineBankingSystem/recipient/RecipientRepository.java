package com.example.OnlineBankingSystem.recipient;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipientRepository extends JpaRepository<Recipient, Long> {

    @Query("SELECT p FROM Recipient p WHERE p.name = :name AND p.user.id = :id")
    Recipient findByName(String name, Long id);

    @Modifying
    @Query("DELETE FROM Recipient p WHERE p.name = :name AND p.user.id = :id")
    void deleteByName(String name, Long id);

}
