package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

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
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/list";
    }

    @GetMapping("/new")
    public String newUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleService.findAll());
        return "admin/new";
    }

    @PostMapping
    public String createUser(@ModelAttribute User user,
                             @RequestParam("roleIds") Set<Long> roleIds) {
        userService.save(user, roleIds);
        return "redirect:/admin";
    }

    @GetMapping("/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        User user = userService.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        model.addAttribute("user", user);
        model.addAttribute("allRoles", roleService.findAll());
        return "admin/edit";
    }

    @PutMapping("/{id}")
    public String updateUser(@PathVariable Long id,
                             @ModelAttribute User user,
                             @RequestParam("roleIds") Set<Long> roleIds) {
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