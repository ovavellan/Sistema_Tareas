package com.proyecto.servicio.auth;

import com.proyecto.dto.SignupRequest;
import com.proyecto.dto.UserDto;
import com.proyecto.enums.UserRole;
import com.proyecto.entities.Usuario;
import com.proyecto.repositorio.UsuarioRepositorio;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UsuarioRepositorio usuarioRepositorio;

    @PostConstruct
    public void createAnAdminAccount(){
        Optional<Usuario> optionalUser = usuarioRepositorio.findByUserRole(UserRole.ADMINISTRADOR);
        if(optionalUser.isEmpty()){
            Usuario usuario = new Usuario();
            usuario.setEmail("admin@test.com");
            usuario.setName("admin");
            usuario.setPassword(new BCryptPasswordEncoder().encode("admin"));
            usuario.setUserRole(UserRole.ADMINISTRADOR);
            usuarioRepositorio.save(usuario);
            System.out.println("Administrador creado correctamente");
        } else{
            System.out.println("La cuenta de administrador ya existe");
        }
    }

    @Override
    public UserDto signupUser(SignupRequest signupRequest) {
        Usuario usuario = new Usuario();
        usuario.setEmail(signupRequest.getEmail());
        usuario.setName(signupRequest.getName());
        usuario.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
        usuario.setUserRole(UserRole.ESTUDIANTE);
        Usuario createdUser = usuarioRepositorio.save(usuario);
        return createdUser.getUserDto();
    }

    @Override
    public boolean hasUserWithEmail(String email) {
        return usuarioRepositorio.findFirstByEmail(email).isPresent();
    }
}
