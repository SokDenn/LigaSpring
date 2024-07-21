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
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleRepo roleRepo;

    @GetMapping
    public String users(Model model) {

        model.addAttribute("roles", roleRepo.findAll());
        model.addAttribute("userAuthentication", userService.returnAuthenticationUserStr());
        model.addAttribute("users", userRepo.findAll());
        return "users";
    }

    @GetMapping({"editUser/{userId}", "/editUser"})
    public String editUser(@PathVariable(name = "userId", required = false) UUID userId,
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

    @PostMapping("/{userId}/role/{roleId}")
    public String addRoleToUser(@PathVariable(name = "userId", required = false) UUID userId,
                                @PathVariable("roleId") UUID roleId) {
        User user = userRepo.findById(userId).orElse(null);
        Role role = roleRepo.findById(roleId).orElse(null);

        if(user != null && role != null){
            if(user.getRoles().add(role)){
                userRepo.save(user);
            }
        }

        return "redirect:/users";
    }

    @DeleteMapping("/{userId}/role/{roleId}")
    public String removeRoleToUser(@PathVariable(name = "userId") UUID userId,
                                   @PathVariable("roleId") UUID roleId) {
        User user = userRepo.findById(userId).orElse(null);
        Role role = roleRepo.findById(roleId).orElse(null);

        if(user != null && role != null){
            if(user.getRoles().remove(role)){
                userRepo.save(user);
            }
        }

        return "redirect:/users";
    }


    @DeleteMapping("/{userId}")
    public String deleteTask(@PathVariable("userId") UUID userId) {

        userService.deleteUser(userId);

        return "redirect:/users";
    }
}
