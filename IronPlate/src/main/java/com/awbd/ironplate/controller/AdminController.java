package com.awbd.ironplate.controller;

import com.awbd.ironplate.domain.User;
import com.awbd.ironplate.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("roles", User.Role.values());
        return "admin/users";
    }

    @PostMapping("/users/{id}/role")
    public String changeRole(@PathVariable Long id,
                             @RequestParam User.Role role,
                             RedirectAttributes redirectAttributes) {
        User user = userService.findById(id);
        user.setRole(role);
        userService.save(user);
        redirectAttributes.addFlashAttribute("success", "Role updated for " + user.getUsername());
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        User user = userService.findById(id);
        userService.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "User '" + user.getUsername() + "' deleted.");
        return "redirect:/admin/users";
    }
}
