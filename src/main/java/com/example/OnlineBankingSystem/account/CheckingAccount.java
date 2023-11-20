package com.example.OnlineBankingSystem.account;

import com.example.OnlineBankingSystem.transaction.CheckingAccountTransaction;
import com.example.OnlineBankingSystem.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name="checkingAccounts")
public class CheckingAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int accountNumber;
    private BigDecimal balance;

    @OneToMany(mappedBy = "checkingAccount")
    private List<CheckingAccountTransaction> checkingAccountTransactionList;

}
