package com.example.OnlineBankingSystem.transaction;

import com.example.OnlineBankingSystem.account.SavingsAccount;
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
@Table(name="savingsAccountTransactions")
public class SavingsAccountTransaction extends Transaction{

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
    @JoinColumn(name = "savings_account_id")
    private SavingsAccount savingsAccount;

    public SavingsAccountTransaction(SavingsAccount savingsAccount, String transferFrom, String transferTo,
                                     String title, BigDecimal amount) {
        this.savingsAccount = savingsAccount;
        this.transferFrom = transferFrom;
        this.transferTo = transferTo;
        this.title = title;
        this.amount = amount;
        this.availableBalance = savingsAccount.getBalance();
    }
}
