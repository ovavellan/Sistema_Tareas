package com.proyecto.servicio.jwt;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UsuarioServicio {

    UserDetailsService userDetailsService();
}
