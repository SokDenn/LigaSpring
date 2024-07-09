package org.example.controller;

import org.example.model.Project;
import org.example.repos.ProjectRepo;
import org.example.repos.UserRepo;
import org.example.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProjectController {
    @Autowired
    private ProjectRepo projectRepo;
    @Autowired
    private UserRepo userRepo;
    private final ProjectService projectService;
    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/projects")
    public String main(Model model) {


        model.addAttribute("users", userRepo.findAll());
        model.addAttribute("projects", projectRepo.findAll());

        return "projects";
    }

    @PostMapping("/addProject")
    public String addTask(@RequestParam("projectId") Long projectId,
                          @RequestParam("heading") String heading,
                          @RequestParam("description") String description) {

        projectService.addProject(projectId, heading, description);

        return "redirect:/projects";
    }

    @PostMapping("/addUserToProject")
    public String addUserToProject(@RequestParam("projectId") Long projectId,
                                   @RequestParam("userId") Long userId) {

        projectService.UserToProject(projectId, userId, 0);

        return "redirect:/projects";
    }

    @PostMapping("/removeUserToProject")
    public String removeUserToProject(@RequestParam("projectId") Long projectId,
                                   @RequestParam("userId") Long userId) {

        projectService.UserToProject(projectId, userId, 1);

        return "redirect:/projects";
    }

    @PostMapping("/deleteProject")
    public String deleteTask(@RequestParam("projectId") Long projectId) {
        Project project = projectRepo.findById(projectId).orElse(null);
        projectRepo.delete(project);

        return "redirect:/projects";
    }
}
