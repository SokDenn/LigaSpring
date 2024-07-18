package org.example.security;

import org.example.dto.UserDTO;
import org.springframework.security.core.Authentication;
import org.example.model.Task;
import org.example.model.User;
import org.springframework.stereotype.Component;

@Component
public class SecurityValidator {
    public boolean taskValidate(Task task, Authentication authentication) {
        boolean isAdmin = authentication
                .getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ADMIN") || a.getAuthority().equals("DIRECTOR"));

        if(!isAdmin) {
            UserDTO currentUser = (UserDTO) authentication.getPrincipal();
            return task.getUser().getId().equals(currentUser.getId());
        }
        return true;
    }
}
