package com.proyecto.dto;

import com.proyecto.enums.UserRole;
import lombok.Data;

@Data
public class AuthenticationResponse {
    private String jwt;
    private long userId;
    private UserRole userRole;
}
