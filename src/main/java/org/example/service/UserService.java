package org.example.service;

import org.example.model.Project;
import org.example.model.Task;
import org.example.model.User;
import org.example.repos.ProjectRepo;
import org.example.repos.TaskRepo;
import org.example.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepo userRepo;
    private final TaskRepo taskRepo;
    private final ProjectRepo projectRepo;
    private  final ProjectService projectService;
    @Autowired
    public UserService(UserRepo userRepo, TaskRepo taskRepo, ProjectRepo projectRepo, ProjectService projectService) {
        this.userRepo = userRepo;
        this.taskRepo = taskRepo;
        this.projectRepo = projectRepo;
        this.projectService = projectService;
    }

    public Boolean addUser(String name, String login, String password) {

        User newUser = new User(name, login, password);
        User findUser = userRepo.findByLogin(newUser.getLogin()).orElse(null);

        if (findUser != null) {
            findUser.setName(name);
            findUser.setPassword(password);

            userRepo.save(findUser);
            System.out.printf("Пользователь с login = %s перезаписан\n", findUser.getLogin());
        } else {
            userRepo.save(newUser);
        }
        return true;
    }

    public void deleteUser(Long id) {
        User user = userRepo.findById(id).orElse(null);
        List<Task> taskList = taskRepo.findByUserId(id);
        List<Project> projectList = projectRepo.findByUsers_Id(id);

        if(!taskList.isEmpty()){
            for (Task task : taskList){
                taskRepo.delete(task);
            }
        }
        if (!projectList.isEmpty()){
            for (Project project : projectList){
                projectService.UserToProject(project.getId(), id, 1);
            }
        }
        userRepo.delete(user);

    }
}
