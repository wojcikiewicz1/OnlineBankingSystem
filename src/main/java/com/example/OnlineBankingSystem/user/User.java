package com.example.OnlineBankingSystem.user;

import com.example.OnlineBankingSystem.account.CheckingAccount;
import com.example.OnlineBankingSystem.account.SavingsAccount;
import com.example.OnlineBankingSystem.recipient.Recipient;
import com.example.OnlineBankingSystem.role.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true)
    private String username;

    @Column(nullable=false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable=false)
    private String firstName;

    @Column(nullable=false)
    private String lastName;

    @OneToOne
    private CheckingAccount checkingAccount;

    @OneToOne
    private SavingsAccount savingsAccount;

    @OneToMany
    @JoinTable(
            name = "users_recipients",
            joinColumns = @JoinColumn(name ="user_id"),
            inverseJoinColumns = @JoinColumn(name = "recipient_id")
    )
    private List<Recipient> recipientList;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name ="user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles;
}
