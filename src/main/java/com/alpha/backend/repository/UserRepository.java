package com.alpha.backend.repository;


import com.alpha.backend.entity.ApplicationUser;
import com.alpha.backend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<ApplicationUser, Integer> {

  Optional<ApplicationUser> findByEmail(String email);

  List<ApplicationUser> findByRole(String role);

}
