package org.example.controller;

import org.example.model.User;
import org.example.repos.UserRepo;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {
    @Autowired
    private UserRepo userRepo;
    private final UserService userService;
    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String registration(){
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Model model) {

        if (userRepo.findByUsername(user.getUsername()).isPresent()) {
            model.addAttribute("message", "Пользователь с таким логином уже существует!");
            return "registration";
        }

        userService.addUser(null, user.getName(), user.getUsername(), user.getPassword());
        return "redirect:/login";
    }
}
