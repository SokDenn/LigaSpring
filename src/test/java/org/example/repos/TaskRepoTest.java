package org.example.repos;

import org.example.dto.TaskDTO;
import org.example.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class TaskRepoTest {

    @Autowired
    private TaskRepo taskRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ProjectRepo projectRepo;
    private Project project1;
    private User user1, user2;

    @BeforeEach
    void setUp() {
        taskRepo.deleteAll();
        user1 = new User("Дачник", "login1", "123123");
        user2 = new User("Шашлык", "login2", "123123");
        project1 = new Project("Заголовок", "Описание");

        projectRepo.save(project1);
        userRepo.save(user1);
        userRepo.save(user2);

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setHeading("Задача 1");
        taskDTO.setDescription("Описание 1");
        taskDTO.setDateOfCompletion(LocalDate.now());
        taskDTO.setUser(user1);
        taskDTO.setStatus(Status.NEW);
        taskDTO.setProject(project1);
        Task task1 = new Task(taskDTO);

        taskDTO.setHeading("Задача 2");
        taskDTO.setDescription("Описание 2");
        Task task2 = new Task(taskDTO);

        taskDTO.setHeading("Задача 3");
        taskDTO.setDescription("Описание 3");
        taskDTO.setUser(user2);
        taskDTO.setStatus(Status.IN_WORK);
        Task task3 = new Task(taskDTO);

        taskDTO.setHeading("Задача 4");
        taskDTO.setDescription("Описание 4");
        taskDTO.setStatus(Status.DONE);
        Task task4 = new Task(taskDTO);

        taskRepo.save(task1);
        taskRepo.save(task2);
        taskRepo.save(task3);
        taskRepo.save(task4);
    }

    @Test
    void testFindByUserIdAndStatus() {
        List<Task> tasks = taskRepo.findByUserIdAndStatus(user1.getId(), Status.NEW);

        assertEquals(2L, tasks.size());
        assertEquals("Задача 1",tasks.get(0).getHeading());
        assertEquals("Описание 2",tasks.get(1).getDescription());
    }

    @Test
    void testFindByUserId() {
        List<Task> tasks = taskRepo.findByUserId(user2.getId());

        assertEquals(2, tasks.size());
        assertEquals("Задача 3",tasks.get(0).getHeading());
        assertEquals("Описание 4",tasks.get(1).getDescription());
    }

    @Test
    void testFindByStatus() {
        List<Task> tasksNew = taskRepo.findByStatus(Status.NEW);
        List<Task> tasksInWork = taskRepo.findByStatus(Status.IN_WORK);

        assertAll(
                () ->assertEquals(2, tasksNew.size()),
                () ->assertEquals(Status.NEW, tasksNew.get(0).getStatus()),
                () -> assertEquals("Задача 1",tasksNew.get(0).getHeading()),

                () -> assertEquals(1, tasksInWork.size()),
                () -> assertEquals(Status.IN_WORK, tasksInWork.get(0).getStatus()),
                () -> assertEquals("Задача 3",tasksInWork.get(0).getHeading())
        );
    }

    @Test
    void testFindAll() {
        Iterable<Task> tasks = taskRepo.findAll();
        assertEquals(4, ((List<Task>) tasks).size());
    }
}