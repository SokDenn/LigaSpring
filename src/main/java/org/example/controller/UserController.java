package org.example.controller;

import org.example.model.Project;
import org.example.model.User;
import org.example.repos.ProjectRepo;
import org.example.repos.UserRepo;
import org.example.service.ProjectService;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {
    @Autowired
    private UserRepo userRepo;
    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String main(Model model) {

        model.addAttribute("users", userRepo.findAll());

        return "users";
    }

    @PostMapping("/addUser")
    public String addTask(@RequestParam("name") String name,
                          @RequestParam("login") String login,
                          @RequestParam("password") String password) {

        userService.addUser(name, login, password);

        return "redirect:/users";
    }


    @PostMapping("/deleteUser")
    public String deleteTask(@RequestParam("userId") Long userId) {

        userService.deleteUser(userId);

        return "redirect:/users";
    }
}
