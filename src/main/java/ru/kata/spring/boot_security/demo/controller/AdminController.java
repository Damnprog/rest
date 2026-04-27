package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String listUsers(Model model, Principal principal) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("allRoles", roleService.findAll());

        if (principal != null) {
            User currentUser = userService.getUserByUsername(principal.getName());
            model.addAttribute("currentUser", currentUser);
        }
        return "admin/list";
    }

    @PostMapping
    public String createUser(@ModelAttribute User user, @RequestParam("roleIds") Set<Long> roleIds) {
        if (user.getUsername() == null || user.getUsername().isBlank()) {
            user.setUsername(user.getEmail());
        }
        userService.save(user, roleIds);
        return "redirect:/admin";
    }

    @PutMapping("/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute User user, @RequestParam("roleIds") Set<Long> roleIds) {
        user.setId(id);
        userService.update(user, roleIds);
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return "redirect:/admin";
    }
}