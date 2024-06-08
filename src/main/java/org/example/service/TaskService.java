package org.example.service;

import org.example.model.Status;
import org.example.model.Task;
import org.example.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class TaskService {
    private final ArrayList<Task> taskList = new ArrayList<>();
    private final UserService userService;
    @Autowired
    public TaskService(UserService userService) {
        this.userService = userService;
    }

    public ArrayList<Task> returnTaskList(){
        return taskList;
    }

    public ArrayList<Task> returnFilteredTaskList (Integer userId, Integer statusNumber){

        return (ArrayList<Task>) taskList.stream()
                .filter(task -> (userId == null || task.getUserId() == userId) &&
                        (statusNumber == null || task.getStatus().statusNumber() == statusNumber))
                .collect(Collectors.toList());
    }

    public Task createTask(int taskId, String heading,String description, String dateOfCompletionStr, int userId) {
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            LocalDate dateOfCompletion = LocalDate.parse(dateOfCompletionStr, formatter);

            return new Task(taskId,heading, description, dateOfCompletion, userId, Status.NEW);

        }catch (Exception e){
            System.out.println("Ошибка создания задачи!");
            return null;
        }
    }

    public Boolean addTask(Task task) {
        if (userService.findUserById(task.getUserId()) != null) {
            Task findTask = findTaskInAllTask(task.getId());

            if (findTask != null){
                int indexTaskInList = taskList.indexOf(findTask);
                taskList.set(indexTaskInList, task);
                System.out.printf("Задача с ID = %d перезаписана\n", findTask.getId());
            }
            else{
                taskList.add(task);
            }
            return true;

        } else {
            System.out.println("Задача назначается на несуществующий ID пользователя");
            return false;
        }
    }

    public Boolean updateTaskStatus(Integer taskId, Integer statusNumber) {
        if (findTaskInAllTask(taskId) != null) {
            if(Status.numberOfStatus(statusNumber) != null){
                findTaskInAllTask(taskId).setStatus(Status.numberOfStatus(statusNumber));
                return true;

            } else{
                System.out.println("----------------------------------------------");
                System.out.println("Такого статуса нет");
                return false;
            }
        } else {
            System.out.println("Задачи с таким ID нет");
            return false;
        }
    }

    public Task findTaskInAllTask(int taskId){
        for(Task task : taskList){
            if(task.getId() == taskId) return task;
        }
        return null;
    }
}
