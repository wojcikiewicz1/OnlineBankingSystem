package com.example.OnlineBankingSystem.transaction;

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

public class Transaction {

    private String transferFrom;
    private String transferTo;
    private String title;
    private BigDecimal amount;
    @CreationTimestamp
    private Date operationDate;
    private BigDecimal availableBalance;
}
