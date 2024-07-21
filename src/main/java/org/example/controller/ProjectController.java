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
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@PreAuthorize("hasAuthority('DIRECTOR') or hasAuthority('ADMIN')")
@RequestMapping("/projects")
public class ProjectController {
    @Autowired
    private ProjectRepo projectRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserService userService;

    @GetMapping
    public String projects(Model model) {

        model.addAttribute("userAuthentication", userService.returnAuthenticationUserStr());
        model.addAttribute("users", userRepo.findAll());
        model.addAttribute("projects", projectRepo.findAll());

        return "projects";
    }

    @GetMapping({"editProject/{projectId}", "/editProject"})
    public String projectEdit(@PathVariable(name = "projectId", required = false) UUID projectId,
                       Model model){

        if (projectId != null) {
            model.addAttribute("project", projectRepo.findById(projectId).orElse(null));
        }
        return "editProject";
    }

    @PostMapping("/addProject")
    public String addTask(@RequestParam(name = "projectId", required = false) UUID projectId,
                          @RequestParam("heading") String heading,
                          @RequestParam("description") String description) {

        projectService.addProject(projectId, heading, description);

        return "redirect:/projects";
    }

    @PostMapping("/{projectId}/user/{userId}")
    public String addUserToProject(@PathVariable("projectId") UUID projectId,
                                   @PathVariable("userId") UUID userId) {

        projectService.UserToProject(projectId, userId, 0);

        return "redirect:/projects";
    }

    @DeleteMapping("/{projectId}/user/{userId}")
    public String removeUserToProject(@PathVariable("projectId") UUID projectId,
                                      @PathVariable("userId") UUID userId) {

        projectService.UserToProject(projectId, userId, 1);

        return "redirect:/projects";
    }

    @DeleteMapping("/{projectId}")
    public String deleteTask(@PathVariable("projectId") UUID projectId) {

        Project project = projectRepo.findById(projectId).orElse(null);
        projectRepo.delete(project);

        return "redirect:/projects";
    }
}
