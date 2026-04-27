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


    private UserDto convertToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setAge(user.getAge());
        dto.setEmail(user.getEmail());
        Set<String> roleNames = user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toSet());
        dto.setRoles(roleNames);
        return dto;
    }


    private User convertToEntity(UserDto dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setAge(dto.getAge());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());   // будет закодирован в сервисе
        return user;
    }

    @GetMapping("/users")
    public List<UserDto> getAllUsers() {
        return userService.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return userService.findById(id).map(user -> ResponseEntity.ok(convertToDto(user))).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/users")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        if (userDto.getUsername() == null || userDto.getUsername().isBlank()) {
            userDto.setUsername(userDto.getEmail());
        }
        User user = convertToEntity(userDto);
        Set<Long> roleIds = userDto.getRoles().stream().map(roleName -> roleService.findByName(roleName)).filter(Objects::nonNull).map(role -> role.getId()).collect(Collectors.toSet());
        User saved = userService.save(user, roleIds);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDto(saved));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        userDto.setId(id);
        User user = convertToEntity(userDto);
        Set<Long> roleIds = userDto.getRoles().stream().map(roleName -> roleService.findByName(roleName)).filter(Objects::nonNull).map(role -> role.getId()).collect(Collectors.toSet());
        User updated = userService.update(user, roleIds);
        return ResponseEntity.ok(convertToDto(updated));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}