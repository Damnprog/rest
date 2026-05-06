package ru.kata.spring.boot_security.demo.controller.rest;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dto.UserDto;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AdminRestController {

    private final UserService userService;
    private final RoleService roleService;

    public AdminRestController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/users")
    public List<UserDto> getAllUsers() {
        return userService.findAll().stream()
                .map(userService::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return userService.findById(id)
                .map(user -> ResponseEntity.ok(userService.convertToDto(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/users")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        User user = userService.convertToEntity(userDto);
        Set<Long> roleIds = userDto.getRoles().stream()
                .map(roleService::findByName)
                .filter(Objects::nonNull)
                .map(role -> role.getId())
                .collect(Collectors.toSet());
        User saved = userService.save(user, roleIds);
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.convertToDto(saved));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        userDto.setId(id);
        User user = userService.convertToEntity(userDto);
        Set<Long> roleIds = userDto.getRoles().stream()
                .map(roleService::findByName)
                .filter(Objects::nonNull)
                .map(role -> role.getId())
                .collect(Collectors.toSet());
        User updated = userService.update(user, roleIds);
        return ResponseEntity.ok(userService.convertToDto(updated));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}