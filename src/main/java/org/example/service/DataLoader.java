package org.example.service;

import com.opencsv.CSVReader;
import jakarta.annotation.PostConstruct;
import org.example.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Collectors;
@Component
public class DataLoader {
    //String csvFileUser = "/home/denis/Рабочий стол/user.csv";
    //String csvFileTask = "/home/denis/Рабочий стол/task.csv";
    private final UserService userService;
    private final TaskService taskService;
    private final String csvFileUser = "C:\\Users\\denis\\Desktop\\user.csv";
    private final String csvFileTask = "C:\\Users\\denis\\Desktop\\task.csv";

    @Autowired
    public DataLoader(UserService userService, TaskService taskService) {
        this.userService = userService;
        this.taskService = taskService;
    }

    @PostConstruct
    public void init() {
        downloadFromFile(0);
        downloadFromFile(1);
    }

    public void saveAll() {
        saveToFile(0);
        saveToFile(1);
    }

    public void downloadFromFile(int switchToTaskLoading) {
        String csvFile = switchToTaskLoading == 0 ? csvFileUser : csvFileTask;

        try (CSVReader rider = createCSVReader(new FileReader(csvFile))) {
            String[] massStrCsv;

            while ((massStrCsv = rider.readNext()) != null) {
                try {
                    Integer id = Integer.valueOf(massStrCsv[0]);

                    if (switchToTaskLoading == 0) {
                        String name = massStrCsv[1];
                        if (id == null || name == null || name.isEmpty()) {
                            throw new NumberFormatException();
                        } else {
                            userService.returnUserList().add(new User(id, name));
                        }
                    } else {
                        String heading = massStrCsv[1];
                        String description = massStrCsv[2];
                        Integer userId = Integer.valueOf(massStrCsv[3]);
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                        LocalDate dateOfCompletion = LocalDate.parse(massStrCsv[4], formatter);
                        Status status = massStrCsv.length > 5 ? Status.titleOfStatus(massStrCsv[5]) : Status.NEW;

                        if (id == null || heading == null || userId == null ||
                                dateOfCompletion == null || status == null ||
                                heading.isEmpty() || description.isEmpty()) {
                            throw new NumberFormatException();
                        } else {
                            Task task = new Task(id, heading, description, dateOfCompletion, userId, status);

                            if (!taskService.addTask(task)) {
                                throw new NumberFormatException();
                            }
                        }
                    }

                } catch (Exception e) {
                    System.out.println("Ошибка загрузки из файла строки:");
                    String result = Arrays.stream(massStrCsv)
                            .map(element -> element + ", ")
                            .collect(Collectors.joining());
                    System.out.println(result);
                }
            }
        } catch (Exception e) {
            System.out.println("Ошибка открытия файла");
        }
    }

    public void saveToFile(int switchToTaskLoading) {
        String csvFile = switchToTaskLoading == 0 ? csvFileUser : csvFileTask;

        try (FileWriter fileWriter = createFileWriter(csvFile)) {

            if (switchToTaskLoading == 0) {
                for (User user : userService.returnUserList()) {
                    fileWriter.write(user.getFullInfoStr());
                }
                System.out.println("Пользователи сохранены!");
            } else {
                for (Task task : taskService.returnTaskList()) {
                    fileWriter.write(task.getFullInfoStr());
                }
                System.out.println("Задачи сохранены!");
            }

        } catch (Exception e) {
            System.out.println("Ошибка записи в исходный файл");
        }
    }
    //Фабричные методы для тестирования
    CSVReader createCSVReader(FileReader fileReader) throws IOException {
        return new CSVReader(fileReader);
    }

    FileWriter createFileWriter(String fileName) throws IOException {
        return new FileWriter(fileName);
    }
}
