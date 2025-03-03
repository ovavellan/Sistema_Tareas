package com.proyecto.service.auth;

import com.proyecto.dto.SignupRequest;
import com.proyecto.dto.UserDto;

public interface AuthService {
    UserDto signupUser(SignupRequest signupRequest);

    boolean hasUserWithEmail(String email);
}
