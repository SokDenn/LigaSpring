package org.example.security;

import org.example.Jwt.JwtRequest;
import org.example.dto.UserDTO;
import org.example.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class SecurityValidator {
    @Autowired
    private AuthenticationManager authenticationManager;

    public boolean taskValidate(Task task, Authentication authentication) {
        boolean isAdmin = authentication
                .getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ADMIN") || a.getAuthority().equals("DIRECTOR"));

        if (!isAdmin) {
            UserDTO currentUser = (UserDTO) authentication.getPrincipal();
            return task.getUser().getId().equals(currentUser.getId());
        }
        return true;
    }

    public Authentication authenticate(JwtRequest authenticationRequest) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
        );
    }
}
