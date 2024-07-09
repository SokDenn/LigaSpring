package org.example.service;

import org.example.model.Project;
import org.example.model.User;
import org.example.repos.ProjectRepo;
import org.example.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ProjectService {
    private final UserRepo userRepo;
    private final ProjectRepo projectRepo;
    @Autowired
    public ProjectService(UserRepo userRepo, ProjectRepo projectRepo) {
        this.userRepo = userRepo;
        this.projectRepo = projectRepo;
    }

    public Boolean addProject(Long id, String heading, String description) {

        Project newProject = new Project(id , heading, description);
        Project findProject = projectRepo.findById(newProject.getId()).orElse(null);

        if (findProject != null) {
            findProject.setHeading(newProject.getHeading());
            findProject.setDescription(newProject.getDescription());

            projectRepo.save(findProject);
            System.out.printf("Проект с ID = %d перезаписан\n", findProject.getId());
        } else {
            projectRepo.save(newProject);
        }
        return true;
    }


    public Boolean UserToProject(Long projectId, Long userId, int switchToRemove) {
        Project project = projectRepo.findById(projectId).orElse(null);
        User user = userRepo.findById(userId).orElse(null);

        if(user != null && project != null){
            Set<User> users = project.getUsers();

            if(switchToRemove == 0 && !isUserInProject(projectId, userId))
                users.add(user);

            if(switchToRemove == 1) users.remove(user);

            project.setUsers(users);
            projectRepo.save(project);
            return true;
        }
        else return false;
    }

    public Boolean isUserInProject(Long projectId, Long userId) {
        Project project = projectRepo.findById(projectId).orElse(null);

        if(project != null){
            return project.getUsers().stream()
                    .anyMatch(user -> user.getId().equals(userId));
        }
        else return false;
    }
}
