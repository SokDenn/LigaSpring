package org.example.controller;

import org.example.model.Task;
import org.example.repos.TaskRepo;
import org.example.repos.UserRepo;
import org.example.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TaskController {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private TaskRepo taskRepo;
    private final TaskService taskService;
    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }
    @PostMapping("/updateTaskStatus")
    public String updateTaskStatus(@RequestParam("taskId") int taskId,
                                   @RequestParam("newStatus") int newStatusNumber) {

        taskService.updateTaskStatus(taskId, newStatusNumber);
        return "redirect:/main";
    }

    @PostMapping("/addTask")
    public String addTask(@RequestParam("taskId") int taskId,
                          @RequestParam("heading") String heading,
                          @RequestParam("description") String description,
                          @RequestParam("dateOfCompletion") String dateOfCompletionStr,
                          @RequestParam("userId") Long userId) {

        Task newTask = taskService.createTask(taskId, heading, description, dateOfCompletionStr, userRepo.findById(userId).get());
        if(newTask != null) taskService.addTask(newTask);

        return "redirect:/main";
    }

    @PostMapping("/deleteTask")
    public String deleteTask(@RequestParam("taskId") Integer taskId) {
        Task task = taskRepo.findById((long) taskId).orElse(null);
        taskRepo.delete(task);

        return "redirect:/main";
    }
}
