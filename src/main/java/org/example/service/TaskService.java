package org.example.service;

import org.example.model.Status;
import org.example.model.Task;
import org.example.model.User;
import org.example.repos.TaskRepo;
import org.example.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class TaskService {
    private final TaskRepo taskRepo;
    private final UserRepo userRepo;

    @Autowired
    public TaskService(TaskRepo taskRepo, UserRepo userRepo) {
        this.taskRepo = taskRepo;
        this.userRepo = userRepo;
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

    public Task createTask(int taskId, String heading,String description, String dateOfCompletionStr, User user) {
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            LocalDate dateOfCompletion = LocalDate.parse(dateOfCompletionStr, formatter);

            return new Task(taskId,heading, description, dateOfCompletion, user, Status.NEW);

        }catch (Exception e){
            System.out.println("Ошибка создания задачи!");
            return null;
        }
    }

    public Boolean addTask(Task task) {
        if (userRepo.existsById(task.getUser().getId())) {

            Task findTask = taskRepo.findById((long) task.getId()).orElse(null);
            if (findTask != null){
                findTask.setHeading(task.getHeading());
                findTask.setDescription(task.getDescription());
                findTask.setDateOfCompletion(task.getDateOfCompletion());
                findTask.setUser(task.getUser());
                findTask.setStatus(task.getStatus());
                taskRepo.save(findTask);
                System.out.printf("Задача с ID = %d перезаписана\n", findTask.getId());
            }
            else{
                taskRepo.save(task);
            }
            return true;

        } else {
            System.out.println("Задача назначается на несуществующий ID пользователя");
            return false;
        }
    }

    public Boolean updateTaskStatus(Integer taskId, Integer statusNumber) {
        Task task = taskRepo.findById((long) taskId).orElse(null);
        if (task != null) {
            if(Status.numberOfStatus(statusNumber) != null){
                task.setStatus(Status.numberOfStatus(statusNumber));
                taskRepo.save(task);
                return true;

            } else{
                System.out.println("Такого статуса нет");
                return false;
            }
        } else {
            System.out.println("Задачи с таким ID нет");
            return false;
        }
    }
}
