package com.alpha.backend.repository;

import com.alpha.backend.entity.ApplicationUser;
import com.alpha.backend.entity.Blog;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Integer> {
    List<Blog> getBlogsByUser(ApplicationUser user);
}
