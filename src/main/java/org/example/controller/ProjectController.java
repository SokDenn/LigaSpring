package org.example.controller;

import org.example.model.Project;
import org.example.repos.ProjectRepo;
import org.example.repos.UserRepo;
import org.example.service.ProjectService;
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
@PreAuthorize("hasAuthority('DIRECTOR') or hasAuthority('ADMIN')")
public class ProjectController {
    @Autowired
    private ProjectRepo projectRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserService userService;

    @GetMapping("/projects")
    public String main(Model model) {

        model.addAttribute("userAuthentication", userService.returnAuthenticationUserStr());
        model.addAttribute("users", userRepo.findAll());
        model.addAttribute("projects", projectRepo.findAll());

        return "projects";
    }

    @GetMapping("/projectEdit")
    public String task(@RequestParam(name = "projectId", required = false) UUID projectId,
                       Model model){

        if (projectId != null) {
            model.addAttribute("project", projectRepo.findById(projectId).orElse(null));
        }
        return "projectEdit";
    }

    @PostMapping("/addProject")
    public String addTask(@RequestParam(name = "projectId", required = false) UUID projectId,
                          @RequestParam("heading") String heading,
                          @RequestParam("description") String description) {

        projectService.addProject(projectId, heading, description);

        return "redirect:/projects";
    }

    @PostMapping("/addUserToProject")
    public String addUserToProject(@RequestParam("projectId") UUID projectId,
                                   @RequestParam("userId") UUID userId) {

        projectService.UserToProject(projectId, userId, 0);

        return "redirect:/projects";
    }

    @PostMapping("/removeUserToProject")
    public String removeUserToProject(@RequestParam("projectId") UUID projectId,
                                   @RequestParam("userId") UUID userId) {

        projectService.UserToProject(projectId, userId, 1);

        return "redirect:/projects";
    }

    @PostMapping("/deleteProject")
    public String deleteTask(@RequestParam("projectId") UUID projectId) {
        Project project = projectRepo.findById(projectId).orElse(null);
        projectRepo.delete(project);

        return "redirect:/projects";
    }
}
