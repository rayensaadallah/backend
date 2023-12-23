package com.alpha.backend;

import com.alpha.backend.models.ApplicationUser;
import com.alpha.backend.models.Role;
import com.alpha.backend.repository.RoleRepository;
import com.alpha.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
        System.out.println("Starting in ...");
    }

    @Bean
    CommandLineRunner run(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (roleRepository.findByAuthority("ADMIN").isPresent()) return;

            Role adminRole = roleRepository.save(new Role("ADMIN"));
            Role userRole = roleRepository.save(new Role("USER"));

            ApplicationUser admin = new ApplicationUser(1, "admin", passwordEncoder.encode("0000"), Set.of(adminRole));
            ApplicationUser user = new ApplicationUser(2, "user", passwordEncoder.encode("0000"), Set.of(userRole));
            userRepository.saveAll(List.of(admin, user));
        };
    }


}
