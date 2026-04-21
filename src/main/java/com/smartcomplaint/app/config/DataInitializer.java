package com.smartcomplaint.app.config;

import com.smartcomplaint.app.enums.Role;
import com.smartcomplaint.app.model.User;
import com.smartcomplaint.app.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Guarantee Admin exists with correct password 'password'
        User admin = userRepository.findByEmail("admin@system.com").orElse(new User());
        admin.setEmail("admin@system.com");
        admin.setName("System Admin");
        admin.setPassword(passwordEncoder.encode("password"));
        admin.setRole(Role.ADMIN);
        userRepository.save(admin);

        // Guarantee User exists with correct password 'password'
        User alice = userRepository.findByEmail("alice@gmail.com").orElse(new User());
        alice.setEmail("alice@gmail.com");
        alice.setName("Alice Smith");
        alice.setPassword(passwordEncoder.encode("password"));
        alice.setRole(Role.USER);
        userRepository.save(alice);
        
        System.out.println("=========================================================");
        System.out.println("USER ACCOUNTS INITIALIZED AND PASSWORDS RESET TO 'password'");
        System.out.println("=========================================================");
    }
}
