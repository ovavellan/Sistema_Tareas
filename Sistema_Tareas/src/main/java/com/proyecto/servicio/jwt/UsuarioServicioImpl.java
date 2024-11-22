package com.proyecto.servicio.jwt;

import com.proyecto.repositorio.UsuarioRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioServicioImpl implements UsuarioServicio{

    private final UsuarioRepositorio usuarioRepositorio;

    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return usuarioRepositorio.findFirstByEmail(username).orElseThrow(()->new UsernameNotFoundException("Usuario no encontrado"));
            }
        };
    }
}
