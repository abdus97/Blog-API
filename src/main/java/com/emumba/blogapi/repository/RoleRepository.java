package com.emumba.blogapi.repository;

import com.emumba.blogapi.model.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name); // Method to find role by name
}
