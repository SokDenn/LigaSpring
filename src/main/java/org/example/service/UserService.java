package org.example.service;

import org.example.dto.UserDTO;
import org.example.model.Project;
import org.example.model.Role;
import org.example.model.Task;
import org.example.model.User;
import org.example.repos.ProjectRepo;
import org.example.repos.RoleRepo;
import org.example.repos.TaskRepo;
import org.example.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private TaskRepo taskRepo;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private ProjectRepo projectRepo;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private PasswordEncoder passwordEncoder;


    public Boolean addUser(UUID userId, String name, String login, String password) {
        User newUser = new User(name, login, password);
        if (userRepo.findByUsername(login).isPresent() && userId == null) return false;

        if (userId != null) {
            User findUser = userRepo.findById(userId).orElse(null);
            findUser.setName(name);
            findUser.setUsername(login);
            findUser.setPassword(passwordEncoder.encode(password));
            userRepo.save(findUser);
            System.out.printf("Пользователь с login = %s перезаписан\n", userId);

        } else {
            Role roleUser = roleRepo.findByName("USER").orElse(null);
            Set<Role> roles = new HashSet<>();
            roles.add(roleUser);

            newUser.setRoles(roles);
            newUser.setPassword(passwordEncoder.encode(password));
            userRepo.save(newUser);
        }
        return true;
    }

    public void deleteUser(UUID id) {
        User user = userRepo.findById(id).orElse(null);
        List<Task> taskList = taskRepo.findByUserId(id);
        List<Project> projectList = projectRepo.findByUsersId(id);

        if (!taskList.isEmpty()) {
            for (Task task : taskList) {
                taskRepo.delete(task);
            }
        }
        if (!projectList.isEmpty()) {
            for (Project project : projectList) {
                projectService.UserToProject(project.getId(), id, 1);
            }
        }
        userRepo.delete(user);

    }

    public void removeRoleToUser(UUID userId, UUID roleId) {
        User user = userRepo.findById(userId).orElse(null);
        Role role = roleRepo.findById(roleId).orElse(null);

        if (user != null && role != null) {
            if (user.getRoles().remove(role)) {
                userRepo.save(user);
            }
        }
    }

    public void addRoleToUser(UUID userId, UUID roleId) {
        User user = userRepo.findById(userId).orElse(null);
        Role role = roleRepo.findById(roleId).orElse(null);

        if (user != null && role != null) {
            if (user.getRoles().add(role)) {
                userRepo.save(user);
            }
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user;
        try {
            user = userRepo.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Пользователь с таким логином не найден: " + username));

        } catch (UsernameNotFoundException e) {
            System.out.println(e.getMessage());
            return null;
        }

        return new UserDTO(user);
    }

    public String returnAuthenticationUserStr() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }
}
