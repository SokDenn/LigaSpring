package org.example.controller;

import org.example.model.Status;
import org.example.repos.TaskRepo;
import org.example.repos.UserRepo;
import org.example.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MainController {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private TaskRepo taskRepo;
    private final TaskService taskService;




    @Autowired
    public MainController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/main")
    public String main(@RequestParam(name = "userIdFilter", required = false) Long userIdFilter,
                       @RequestParam(name = "statusNumberFilter", required = false) Integer statusNumberFilter,
                       Model model) {

        //Фильтр
        model.addAttribute("userIdFilter", userIdFilter);
        model.addAttribute("statusNumberFilter", statusNumberFilter);

        model.addAttribute("statuses", Status.values());
        model.addAttribute("tasks", taskService.returnFilteredTaskList(userIdFilter, statusNumberFilter));
        model.addAttribute("users", userRepo.findAll());

        return "main";
    }

    @PostMapping("/deleteAll")
    public String deleteAll(RedirectAttributes redirectAttributes) {

        try{
            taskRepo.deleteAll();
            //userRepo.deleteAll();

            redirectAttributes.addFlashAttribute("message", "Успешно удалено!");
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("message", "Ошибка при удалении: " + e.getMessage());
        }
        return "redirect:/main";
    }

}
