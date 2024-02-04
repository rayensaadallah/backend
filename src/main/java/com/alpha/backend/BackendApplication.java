package com.alpha.backend;

import com.alpha.backend.entity.ApplicationUser;
import com.alpha.backend.entity.Role;
import com.alpha.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Bean
    public CommandLineRunner initUsers(UserRepository userRepository) {
        return args -> {
            // Create the admin user
            ApplicationUser adminUser = new ApplicationUser();
            adminUser.setFirstname("Admin");
            adminUser.setLastname("User");
            adminUser.setEmail("admin@gmail.com");
            String adminPassword = "Admin123"; // Replace with the actual password
            adminUser.setPassword(passwordEncoder.encode(adminPassword));
            adminUser.setMfaEnabled(false);
            adminUser.setSecret(null); // You can set the secret as needed
            adminUser.setPhoneNumber("123-456-7890"); // You can set the phone number as needed
            adminUser.setRole(Role.ADMIN);
            userRepository.save(adminUser);
        };
    }
}
 /*
    docker run -d \
  --name my-postgres-container \
  -p 5432:5432 \
  -e POSTGRES_DB=backend \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -v postgres_data:/var/lib/postgresql/data \
  postgres:latest

      docker start my-postgres-container


     */