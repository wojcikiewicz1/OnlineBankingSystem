package com.example.OnlineBankingSystem.transaction;

import com.example.OnlineBankingSystem.account.CheckingAccount;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name="checkingAccountTransactions")
public class CheckingAccountTransaction {

    //AUTO
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String transferFrom;
    private String transferTo;
    private String title;
    private BigDecimal amount;
    @CreationTimestamp
    private Date operationDate;
    private BigDecimal availableBalance;

    @ManyToOne
    @JoinColumn(name = "checking_account_id")
    private CheckingAccount checkingAccount;



}
