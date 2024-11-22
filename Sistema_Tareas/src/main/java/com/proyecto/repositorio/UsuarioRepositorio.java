package com.proyecto.repositorio;

import com.proyecto.enums.UserRole;
import com.proyecto.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findFirstByEmail(String username);
    Optional<Usuario> findByUserRole(UserRole userRole);
}
