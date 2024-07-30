package org.example.service;

import org.example.model.Project;
import org.example.model.User;
import org.example.repos.ProjectRepo;
import org.example.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
public class ProjectService {
    private final UserRepo userRepo;
    private final ProjectRepo projectRepo;

    @Autowired
    public ProjectService(UserRepo userRepo, ProjectRepo projectRepo) {
        this.userRepo = userRepo;
        this.projectRepo = projectRepo;
    }

    public Boolean addProject(UUID id, String heading, String description) {

        Project newProject = new Project(heading, description);

        if (id != null) {
            newProject.setId(id);
            Set<User> users = projectRepo.findById(id).get().getUsers();
            if (users != null) newProject.setUsers(users);
            System.out.printf("Проект с ID = %s перезаписан\n", newProject.getId());
        } else {
            System.out.printf("Проект с ID = %s добавлен\n", id);
        }
        projectRepo.save(newProject);
        return true;
    }


    public Boolean UserToProject(UUID projectId, UUID userId, int switchToRemove) {
        Project project = projectRepo.findById(projectId).orElse(null);
        User user = userRepo.findById(userId).orElse(null);

        if (user != null && project != null) {
            Set<User> users = project.getUsers();

            if (switchToRemove == 0 && !isUserInProject(projectId, userId)) {
                users.add(user);
            }

            if (switchToRemove == 1 && isUserInProject(projectId, userId)) {
                users.remove(user);
            }

            project.setUsers(users);
            projectRepo.save(project);
            return true;
        } else return false;
    }

    public Boolean isUserInProject(UUID projectId, UUID userId) {
        Project project = projectRepo.findById(projectId).orElse(null);

        if (project != null) {
            return project.getUsers().stream()
                    .anyMatch(user -> user.getId().equals(userId));
        } else return false;
    }

    public Boolean deleteProject(UUID projectId) {
        Project project = projectRepo.findById(projectId).orElse(null);

        if (project != null) {
            projectRepo.delete(project);
            return true;

        } else return false;
    }
}
