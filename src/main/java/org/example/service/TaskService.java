package org.example.service;

import org.example.model.*;
import org.example.repos.ProjectRepo;
import org.example.repos.TaskRepo;
import org.example.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Set;

@Service
public class TaskService {
    private final TaskRepo taskRepo;
    private final UserRepo userRepo;
    private ProjectRepo projectRepo;

    @Autowired
    public TaskService(TaskRepo taskRepo, UserRepo userRepo, ProjectRepo projectRepo) {
        this.taskRepo = taskRepo;
        this.userRepo = userRepo;
        this.projectRepo = projectRepo;
    }

    public List<Task> returnFilteredTaskList(Long userId, Integer statusNumber) {
        if (userId != null && statusNumber != null) {
            Status status = Status.numberOfStatus(statusNumber);
            return taskRepo.findByUserIdAndStatus(userId, status);

        } else if (userId != null) {
            return taskRepo.findByUserId(userId);

        } else if (statusNumber != null) {
            Status status = Status.numberOfStatus(statusNumber);
            return taskRepo.findByStatus(status);

        } else {
            return (List<Task>) taskRepo.findAll();
        }
    }

    public Task createTask(TaskDTO taskDTO) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            LocalDate dateOfCompletion = LocalDate.parse(taskDTO.getDateOfCompletionStr(), formatter);

            taskDTO.setDateOfCompletion(dateOfCompletion);
            taskDTO.setUser(userRepo.findById(taskDTO.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("Пользователя с таким ID нет")));
            taskDTO.setProject(projectRepo.findById(taskDTO.getProjectId())
                    .orElseThrow(() -> new IllegalArgumentException("Проекта с таким ID нет")));

            return new Task(taskDTO);

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (DateTimeParseException e) {
            System.out.println("Ошибка парсинга даты: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Ошибка создания задачи: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public Boolean addTask(Task task) {
        if (!(userRepo.existsById(task.getUser().getId()) && projectRepo.existsById(task.getProject().getId()))) {
            System.out.println("Задача назначается на несуществующий ID пользователя или несуществующий проект");
            return false;
        }
        Set<Task> tasks = projectRepo.findById(task.getProject().getId()).get().getTasks();
        Task findTask = taskRepo.findById(task.getId()).orElse(null);
        if (findTask != null) {
            findTask.setHeading(task.getHeading());
            findTask.setDescription(task.getDescription());
            findTask.setDateOfCompletion(task.getDateOfCompletion());
            findTask.setUser(task.getUser());
            findTask.setStatus(task.getStatus());
            findTask.setProject(task.getProject());
            taskRepo.save(findTask);

            System.out.printf("Задача с ID = %d перезаписана\n", findTask.getId());
        } else {
            taskRepo.save(task);
        }
        return true;
    }

    public Boolean updateTaskStatus(Long taskId, Integer statusNumber) {
        Task task = taskRepo.findById(taskId).orElse(null);

        if (task != null) {
            if (Status.numberOfStatus(statusNumber) != null) {
                task.setStatus(Status.numberOfStatus(statusNumber));
                taskRepo.save(task);
                return true;

            } else {
                System.out.println("Такого статуса нет");
                return false;
            }
        } else {
            System.out.println("Задачи с таким ID нет");
            return false;
        }
    }
}
