package org.example.controller;

import org.example.model.Status;
import org.example.repos.ProjectRepo;
import org.example.repos.TaskRepo;
import org.example.repos.UserRepo;
import org.example.service.TaskService;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
@PreAuthorize("hasAuthority('USER') or hasAuthority('DIRECTOR') or hasAuthority('ADMIN')")
public class MainController {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private TaskRepo taskRepo;
    @Autowired
    private ProjectRepo projectRepo;
    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;


    @GetMapping("/main")
    public String main(@RequestParam(name = "userIdFilter", required = false) UUID userIdFilter,
                       @RequestParam(name = "statusNumberFilter", required = false) Integer statusNumberFilter,
                       @RequestParam(name = "message", required = false) String message,
                       Model model) {

        //Фильтр
        model.addAttribute("userIdFilter", userIdFilter);
        model.addAttribute("statusNumberFilter", statusNumberFilter);

        // Сообщение
        if (message != null && !message.isEmpty()) {
            model.addAttribute("message", message);
        }

        model.addAttribute("userAuthentication", userService.returnAuthenticationUserStr());
        model.addAttribute("statuses", Status.values());
        model.addAttribute("tasks", taskService.returnFilteredTaskList(userIdFilter, statusNumberFilter));
        model.addAttribute("users", userRepo.findAll());
        model.addAttribute("projects", projectRepo.findAll());

        return "main";
    }
}
