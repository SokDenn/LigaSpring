package org.example.controller;
import org.example.model.Status;
import org.example.model.Task;
import org.example.model.User;
import org.example.service.DataLoader;
import org.example.service.TaskService;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class MainController {
    private final TaskService taskService;
    private final UserService userService;
    private final DataLoader dataLoader;

    @Autowired
    public MainController(TaskService taskService, UserService userService, DataLoader dataLoader) {
        this.taskService = taskService;
        this.userService = userService;
        this.dataLoader = dataLoader;
    }

    @GetMapping("/main")
    public String main(@RequestParam(name = "userIdFilter", required = false) Integer userIdFilter,
                       @RequestParam(name = "statusNumberFilter", required = false) Integer statusNumberFilter,
                       Model model) {

        Map<Integer, String> userNamesMap = userService.returnUserList()
                .stream().collect(Collectors.toMap(User::getId, User::getName));

        //Фильтр
        model.addAttribute("userIdFilter", userIdFilter);
        model.addAttribute("statusNumberFilter", statusNumberFilter);

        model.addAttribute("userNamesMap", userNamesMap);
        model.addAttribute("statuses", Status.values());
        model.addAttribute("tasks", taskService.returnFilteredTaskList(userIdFilter, statusNumberFilter));
        model.addAttribute("users", userService.returnUserList());

        return "main";
    }

    @PostMapping("/saveAll")
    public String saveAll(RedirectAttributes redirectAttributes) {

        try{
            dataLoader.saveAll();
            redirectAttributes.addFlashAttribute("message", "Успешно сохранено!");
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("message", "Ошибка при сохранении: " + e.getMessage());
        }
        return "redirect:/main";
    }

    @PostMapping("/deleteAll")
    public String deleteAllTasks(RedirectAttributes redirectAttributes) {

        try{
            taskService.returnTaskList().clear();
            userService.returnUserList().clear();
            //dataLoader.saveAll();
            redirectAttributes.addFlashAttribute("message", "Успешно удалено!");
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("message", "Ошибка при удалении: " + e.getMessage());
        }
        return "redirect:/main";
    }

}
