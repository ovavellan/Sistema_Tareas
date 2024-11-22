package com.proyecto.dto;

import com.proyecto.enums.UserRole;
import lombok.Data;

@Data
public class UserDto {

    private long id;
    private String name;
    private String email;
    private String password;
    private UserRole userRole;
}
