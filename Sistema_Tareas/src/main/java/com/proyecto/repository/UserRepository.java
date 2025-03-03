package com.proyecto.repository;

import com.proyecto.enums.UserRole;
import com.proyecto.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findFirstByEmail(String username);
    Optional<User> findByUserRole(UserRole userRole);
}
