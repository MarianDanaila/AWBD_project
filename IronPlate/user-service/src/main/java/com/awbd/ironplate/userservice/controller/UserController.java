package com.awbd.ironplate.userservice.controller;

import com.awbd.ironplate.userservice.domain.User;
import com.awbd.ironplate.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/auth/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> credentials) {
        String token = userService.login(credentials.get("username"), credentials.get("password"));
        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/auth/register")
    public ResponseEntity<User> register(@Valid @RequestBody User user) {
        return ResponseEntity.ok(userService.register(user));
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PutMapping("/users/{id}/role")
    public ResponseEntity<User> updateRole(@PathVariable Long id, @RequestParam User.Role role) {
        return ResponseEntity.ok(userService.updateRole(id, role));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
