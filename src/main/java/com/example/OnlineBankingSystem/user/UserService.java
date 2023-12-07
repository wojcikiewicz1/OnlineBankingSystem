package com.example.OnlineBankingSystem.user;

import com.example.OnlineBankingSystem.account.AccountService;
import com.example.OnlineBankingSystem.role.Role;
import com.example.OnlineBankingSystem.role.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountService accountService;

    public void saveUser(User user) {
        User user1 = new User();
        user1.setUsername(user.getUsername());
        user1.setPassword(passwordEncoder.encode(user.getPassword()));
        user1.setEmail(user.getEmail());
        user1.setFirstName(user.getFirstName());
        user1.setLastName(user.getLastName());
        user1.setRecipientList(user.getRecipientList());
        user1.setCheckingAccount(accountService.createCheckingAccount());
        user1.setSavingsAccount(accountService.createSavingsAccount());

        Role role = roleRepository.findByName("USER");
        if(role == null){
            role = checkRoleExist();
        }
        user1.setRoles(Arrays.asList(role));
        userRepository.save(user1);

    }

    private Role checkRoleExist(){
        Role role = new Role();
        role.setName("USER");
        return roleRepository.save(role);
    }

    public User findByUserName(String username) {
        return userRepository.findByUsername(username);
    }

    public void updateUser (User user) {
        userRepository.save(user);
    }

    public boolean verifyUserPassword(User user, String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(password, user.getPassword());
    }

    public void changePassword (User user, String newPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(newPassword));
        userRepository.save(user);
    }
}
