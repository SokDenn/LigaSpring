package org.example.controller;

import org.example.model.Role;
import org.example.model.User;
import org.example.repos.RoleRepo;
import org.example.repos.UserRepo;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleRepo roleRepo;

    @GetMapping("/users")
    public String users(Model model) {

        model.addAttribute("roles", roleRepo.findAll());
        model.addAttribute("userAuthentication", userService.returnAuthenticationUserStr());
        model.addAttribute("users", userRepo.findAll());
        return "users";
    }

    @GetMapping("/editUser")
    public String editUser(@RequestParam(name = "userId", required = false) UUID userId,
                           Model model) {

        if (userId != null) {
            model.addAttribute("user", userRepo.findById(userId).orElse(null));
        }
        return "editUser";
    }

    @PostMapping("/addUser")
    public String addTask(@RequestParam(name = "userId", required = false) UUID userId,
                          @RequestParam("name") String name,
                          @RequestParam("username") String username,
                          @RequestParam("password") String password) {

        userService.addUser(userId, name, username, password);

        return "redirect:/users";
    }

    @PostMapping("/addRoleToUser")
    public String addRoleToUser(@RequestParam(name = "userId", required = false) UUID userId,
                          @RequestParam("roleId") UUID roleId) {
        User user = userRepo.findById(userId).orElse(null);
        Role role = roleRepo.findById(roleId).orElse(null);

        if(user != null && role != null){
            if(user.getRoles().add(role)){
                userRepo.save(user);
            }
        }

        return "redirect:/users";
    }

    @PostMapping("/removeRoleToUser")
    public String removeRoleToUser(@RequestParam(name = "userId", required = false) UUID userId,
                                @RequestParam("roleId") UUID roleId) {
        User user = userRepo.findById(userId).orElse(null);
        Role role = roleRepo.findById(roleId).orElse(null);

        if(user != null && role != null){
            if(user.getRoles().remove(role)){
                userRepo.save(user);
            }
        }

        return "redirect:/users";
    }


    @PostMapping("/deleteUser")
    public String deleteTask(@RequestParam("userId") UUID userId) {

        userService.deleteUser(userId);

        return "redirect:/users";
    }
}
