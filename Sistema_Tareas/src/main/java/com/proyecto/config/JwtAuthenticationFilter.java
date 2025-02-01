package com.proyecto.config;

import com.proyecto.service.jwt.UserService;
import com.proyecto.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        // Permitir acceso a endpoints de Actuator sin autenticación
        if (request.getRequestURI().startsWith("/actuator")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        System.out.println("Auth Header recibido: " + authHeader);

        if (StringUtils.isEmpty(authHeader) || !authHeader.startsWith("Bearer ")) {
            System.out.println("Header inválido o vacío");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            jwt = authHeader.substring(7);
            System.out.println("JWT extraído: " + jwt.substring(0, Math.min(jwt.length(), 20)) + "...");

            userEmail = jwtUtil.extractUserName(jwt);
            System.out.println("Email extraído del token: " + userEmail);

            // Verificar autenticación actual
            System.out.println("Auth actual: " + SecurityContextHolder.getContext().getAuthentication());

            if (StringUtils.isNotEmpty(userEmail)) {
                UserDetails userDetails = userService.userDetailsService()
                        .loadUserByUsername(userEmail);
                System.out.println("UserDetails cargado: " + userDetails.getUsername());

                if (jwtUtil.isTokenValid(jwt, userDetails)) {
                    System.out.println("Token validado correctamente");

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    System.out.println("Nueva autenticación establecida: " + authToken);
                } else {
                    System.out.println("Token inválido");
                }
            }
        } catch (Exception e) {
            System.out.println("Error en la autenticación: " + e.getMessage());
            e.printStackTrace();
        }

        filterChain.doFilter(request, response);
    }
}