package com.example.task_3_1_3.init;

import com.example.task_3_1_3.entities.Role;
import com.example.task_3_1_3.entities.User;
import com.example.task_3_1_3.repositories.RoleRepository;
import com.example.task_3_1_3.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import javax.annotation.PostConstruct;
import java.util.Set;

@Configuration
public class DataBaseInitializer {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder encoder;

    @Autowired
    public DataBaseInitializer(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
    }

    @PostConstruct
    public void init() {
        Role adminRole = new Role("ROLE_ADMIN");
        Role userRole = new Role("ROLE_USER");

        roleRepository.save(adminRole);
        roleRepository.save(userRole);

        User adminUser = new User("admin", "admin", (byte) 23, "admin@mail.ru", encoder.encode("admin"));
        adminUser.setRoles(Set.of(adminRole, userRole));
        userRepository.save(adminUser);

        User regularUser = new User("user", "user", (byte) 17, "user@mail.ru", encoder.encode("user"));
        regularUser.setRoles(Set.of(userRole));
        userRepository.save(regularUser);
    }
}