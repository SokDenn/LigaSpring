package org.example.controller;

import org.example.model.Task;
import org.example.model.TaskDTO;
import org.example.repos.TaskRepo;
import org.example.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TaskController {
    @Autowired
    private TaskRepo taskRepo;

    private final TaskService taskService;
    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/updateTaskStatus")
    public String updateTaskStatus(@RequestParam("taskId") Long taskId,
                                   @RequestParam("newStatus") int newStatusNumber) {

        taskService.updateTaskStatus(taskId, newStatusNumber);
        return "redirect:/main";
    }

    @PostMapping("/addTask")
    public String addTask(@RequestParam("taskId") int taskId,
                          @RequestParam("heading") String heading,
                          @RequestParam("description") String description,
                          @RequestParam("dateOfCompletion") String dateOfCompletionStr,
                          @RequestParam("userId") Long userId,
                          @RequestParam("projectId") Long projectId) {

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTaskId(taskId);
        taskDTO.setHeading(heading);
        taskDTO.setDescription(description);
        taskDTO.setDateOfCompletionStr(dateOfCompletionStr);
        taskDTO.setUserId(userId);
        taskDTO.setProjectId(projectId);

        Task newTask = taskService.createTask(taskDTO);
        if(newTask != null) taskService.addTask(newTask);

        return "redirect:/main";
    }

    @PostMapping("/deleteTask")
    public String deleteTask(@RequestParam("taskId") Long taskId) {
        Task task = taskRepo.findById(taskId).orElse(null);
        taskRepo.delete(task);

        return "redirect:/main";
    }
}
