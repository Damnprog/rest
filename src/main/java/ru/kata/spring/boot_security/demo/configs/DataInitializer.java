package ru.kata.spring.boot_security.demo.configs;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleService roleService;
    private final UserService userService;

    public DataInitializer(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }


    @Override
    @Transactional
    public void run(String... args) {
        Role adminRole = roleService.findByName("ROLE_ADMIN");
        if (adminRole == null) {
            adminRole = roleService.save(new Role("ROLE_ADMIN"));
        }
        Role userRole = roleService.findByName("ROLE_USER");
        if (userRole == null) {
            userRole = roleService.save(new Role("ROLE_USER"));
        }

        if (userService.findAll().isEmpty()) {
            User admin = new User();
            admin.setUsername("admin@example.com");
            admin.setPassword("admin");
            admin.setFirstName("admin");
            admin.setLastName("admin");
            admin.setEmail("admin@example.com");
            admin.setAge(35);
            userService.save(admin, Set.of(adminRole.getId(), userRole.getId()));
        }
    }
}