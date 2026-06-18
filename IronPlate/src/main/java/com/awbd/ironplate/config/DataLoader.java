package com.awbd.ironplate.config;

import com.awbd.ironplate.domain.User;
import com.awbd.ironplate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        createIfAbsent("athlete", "athlete123", "athlete@ironplate.com", User.Role.ATHLETE);
        createIfAbsent("coach",   "coach123",   "coach@ironplate.com",   User.Role.COACH);
        createIfAbsent("admin",   "admin123",   "admin@ironplate.com",   User.Role.ADMIN);
    }

    private void createIfAbsent(String username, String password, String email, User.Role role) {
        if (userRepository.findByUsername(username).isEmpty()) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setEmail(email);
            user.setRole(role);
            userRepository.save(user);
        }
    }
}
