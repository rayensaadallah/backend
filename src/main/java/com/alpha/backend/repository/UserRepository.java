package com.alpha.backend.repository;

import java.util.List;
import java.util.Optional;

import com.alpha.backend.models.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<ApplicationUser, Integer> {
	Optional<ApplicationUser> findByUsername(String username);
	ApplicationUser getByUsername(String username);

	boolean existsByUsername(String email);
	List<ApplicationUser> findByAuthorities_Authority(String roleName);

	ApplicationUser getByEmail(String s);
}
