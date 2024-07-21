package org.example.controller;

import org.example.model.Status;
import org.example.model.Task;
import org.example.dto.TaskDTO;
import org.example.repos.ProjectRepo;
import org.example.repos.TaskRepo;
import org.example.repos.UserRepo;
import org.example.security.SecurityValidator;
import org.example.service.TaskService;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@PreAuthorize("hasAuthority('USER') or hasAuthority('DIRECTOR') or hasAuthority('ADMIN')")
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private TaskRepo taskRepo;
    @Autowired
    private ProjectRepo projectRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;
    @Autowired
    private SecurityValidator securityValidator;
    @GetMapping
    public String tasks(@RequestParam(name = "userIdFilter", required = false) UUID userIdFilter,
                       @RequestParam(name = "statusNumberFilter", required = false) Integer statusNumberFilter,
                       @RequestParam(name = "message", required = false) String message,
                       Model model) {

        //Фильтр
        model.addAttribute("userIdFilter", userIdFilter);
        model.addAttribute("statusNumberFilter", statusNumberFilter);

        // Сообщение
        if (message != null && !message.isEmpty()) {
            model.addAttribute("message", message);
        }

        model.addAttribute("userAuthentication", userService.returnAuthenticationUserStr());
        model.addAttribute("statuses", Status.values());
        model.addAttribute("tasks", taskService.returnFilteredTaskList(userIdFilter, statusNumberFilter));
        model.addAttribute("users", userRepo.findAll());
        model.addAttribute("projects", projectRepo.findAll());

        return "tasks";
    }

    @PostMapping("/{taskId}")
    public String updateTaskStatus(@PathVariable("taskId") UUID taskId,
                                   @RequestParam("newStatus") int newStatusNumber,
                                   Authentication authentication,
                                   RedirectAttributes redirectAttributes) {

        Task task = taskService.getTaskById(taskId);
        if(!securityValidator.taskValidate(task, authentication)){
            redirectAttributes.addAttribute("message", "Вы обновляете статус не вашей задачи!");
            return "redirect:/tasks";
        }

        taskService.updateTaskStatus(taskId, newStatusNumber);
        redirectAttributes.addFlashAttribute("message", "Статус задачи успешно обновлен!");
        return "redirect:/tasks";
    }

    @PostMapping("/addTask")
    public String addTask(@RequestParam(name = "taskId", required = false) UUID taskId,
                          @RequestParam("heading") String heading,
                          @RequestParam("description") String description,
                          @RequestParam("dateOfCompletion") String dateOfCompletionStr,
                          @RequestParam("userId") UUID userId,
                          @RequestParam("projectId") UUID projectId,
                          RedirectAttributes redirectAttributes) {


        TaskDTO taskDTO = new TaskDTO();
        if(taskId != null) {
            redirectAttributes.addFlashAttribute("message", "Задача успешно обновлена!");
            taskDTO.setTaskId(taskId);
            taskDTO.setStatus(taskService.getTaskById(taskId).getStatus());

        } else {
            taskDTO.setStatus(Status.NEW);
            redirectAttributes.addFlashAttribute("message", "Задача успешно добавлена!");
        }
        taskDTO.setHeading(heading);
        taskDTO.setDescription(description);
        taskDTO.setDateOfCompletionStr(dateOfCompletionStr);
        taskDTO.setUserId(userId);
        taskDTO.setProjectId(projectId);

        Task newTask = taskService.createTask(taskDTO);
        if(newTask != null) {
            taskRepo.save(newTask);}
        else {
            redirectAttributes.addFlashAttribute("message", "Ошибка добавления задачи! Введите корректные данные");
        }
        return "redirect:/tasks";
    }

    @DeleteMapping("{taskId}")
    public String deleteTask(@PathVariable("taskId") UUID taskId,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {
        Task task = taskRepo.findById(taskId).orElse(null);

        if(!securityValidator.taskValidate(task, authentication)){
            redirectAttributes.addAttribute("message", "Вы не можете удалить не свою задачу!");
            return "redirect:/tasks";
        }
        taskRepo.delete(task);

        return "redirect:/tasks";
    }

    @GetMapping({"/editTask", "/editTask/{taskId}"})
    public String task(@PathVariable(name = "taskId", required = false) UUID taskId,
                       Model model,
                       Authentication authentication,
                       RedirectAttributes redirectAttributes){

        if(taskId != null && !securityValidator.taskValidate(taskRepo.findById(taskId).orElse(null), authentication)){
            redirectAttributes.addAttribute("message", "Вы не можете редактировать не свою задачу!");
            return "redirect:/tasks";
        }

        if (taskId != null) {
            model.addAttribute("task", taskService.getTaskById(taskId));
        }
        model.addAttribute("users", userRepo.findAll());
        model.addAttribute("projects", projectRepo.findAll());

        return "editTask";
    }
}
