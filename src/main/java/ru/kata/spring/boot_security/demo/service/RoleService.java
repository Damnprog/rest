package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.Set;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Transactional(readOnly = true)
    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }

    @Transactional(readOnly = true)
    public Set<Role> findAll() {
        return new HashSet<>(roleRepository.findAll());
    }

    @Transactional
    public Role save(Role role) {
        return roleRepository.save(role);
    }

    @Transactional(readOnly = true)
    public Set<Role> findByIds(Set<Long> ids) {
        return new HashSet<>(roleRepository.findAllById(ids));
    }
}