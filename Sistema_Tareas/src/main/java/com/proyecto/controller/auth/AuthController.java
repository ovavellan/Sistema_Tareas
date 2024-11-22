package com.proyecto.controller.auth;

import com.proyecto.dto.AuthenticationRequest;
import com.proyecto.dto.AuthenticationResponse;
import com.proyecto.dto.SignupRequest;
import com.proyecto.dto.UserDto;
import com.proyecto.entities.Usuario;
import com.proyecto.repositorio.UsuarioRepositorio;
import com.proyecto.servicio.auth.AuthService;
import com.proyecto.servicio.jwt.UsuarioServicio;
import com.proyecto.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final UsuarioRepositorio usuarioRepositorio;

    private final JwtUtil jwtUtil;

    private final UsuarioServicio usuarioServicio;

    private final AuthenticationManager authenticationManager;

    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@RequestBody SignupRequest signupRequest) {
        if(authService.hasUserWithEmail(signupRequest.getEmail()))
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Ya existe un usuario con ese correo electrónico");
        UserDto createdUserDto = authService.signupUser(signupRequest);
        if(createdUserDto == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuario no encontrado");
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUserDto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getEmail(),
                            authenticationRequest.getPassword()
                    )
            );
            final UserDetails userDetails = usuarioServicio.userDetailsService()
                    .loadUserByUsername(authenticationRequest.getEmail());
            Optional<Usuario> optionalUser = usuarioRepositorio.findFirstByEmail(authenticationRequest.getEmail());

            final String jwtToken = jwtUtil.generateToken(userDetails);
            AuthenticationResponse authenticationResponse = new AuthenticationResponse();

            if (optionalUser.isPresent()) {
                authenticationResponse.setJwt(jwtToken);
                authenticationResponse.setUserId(optionalUser.get().getId());
                authenticationResponse.setUserRole(optionalUser.get().getUserRole());
                return ResponseEntity.ok(authenticationResponse);
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");

        } catch (BadCredentialsException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Credenciales inválidas");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error en el servidor: " + e.getMessage());
        }
    }
}
