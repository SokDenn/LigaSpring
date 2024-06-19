package org.example.repos;

import org.example.model.Status;
import org.example.model.Task;
import org.example.model.User;
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
    private User user1, user2;

    @BeforeEach
    void setUp() {
        user1 = new User("Дачник 1");
        user2 = new User("Дачник 2");

        Task task1 = new Task(1, "Задача 1", "Описание 1", LocalDate.now(), user1, Status.NEW);
        Task task2 = new Task(2, "Задача 2", "Описание 2", LocalDate.now(), user1, Status.NEW);
        Task task3 = new Task(3, "Задача 3", "Описание 3", LocalDate.now(), user2, Status.IN_WORK);
        Task task4 = new Task(4, "Задача 4", "Описание 4", LocalDate.now(), user2, Status.DONE);

        userRepo.save(user1);
        userRepo.save(user2);

        taskRepo.save(task1);
        taskRepo.save(task2);
        taskRepo.save(task3);
        taskRepo.save(task4);
    }

    @Test
    void testFindByUserIdAndStatus() {
        List<Task> tasks = taskRepo.findByUserIdAndStatus(user1.getId(), Status.NEW);

        assertEquals(2, tasks.size());
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