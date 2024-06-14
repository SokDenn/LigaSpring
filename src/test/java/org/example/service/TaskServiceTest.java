package org.example.service;
import org.example.model.Status;
import org.example.model.Task;
import org.example.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    @InjectMocks
    private TaskService taskService;
    @Mock
    private UserService userService;


    @BeforeEach
    public void setUp() {
        taskService.returnTaskList().add(new Task(1, "Задача 1", "Текст 1",
                LocalDate.now(), 1, Status.NEW));
        taskService.returnTaskList().add(new Task(2, "Задача 2", "Текст 2",
                LocalDate.now().plusDays(1), 1, Status.DONE));
        taskService.returnTaskList().add(new Task(3, "Задача 3", "Текст 3",
                LocalDate.now().plusDays(2), 2, Status.NEW));
    }

    @Test
    public void testReturnFilteredTaskListByUser() {
        List<Task> filteredTasks = taskService.returnFilteredTaskList(1, null);
        assertEquals(2, filteredTasks.size());
        assertEquals(1, filteredTasks.get(0).getUserId());
    }

    @Test
    public void testReturnFilteredTaskListByStatusNumber() {
        List<Task> filteredTasks = taskService.returnFilteredTaskList(null, 1);
        assertEquals(2, filteredTasks.size());
        assertEquals(1, filteredTasks.get(0).getStatus().statusNumber());
    }

    @Test
    public void testReturnFilteredTaskListByStatusNumberAndUser() {
        List<Task> filteredTasks = taskService.returnFilteredTaskList(1, 1);
        assertEquals(1, filteredTasks.size());
        assertEquals(1, filteredTasks.get(0).getStatus().statusNumber());
        assertEquals(1, filteredTasks.get(0).getUserId());
    }

    @Test
    public void testCreateTask() {
        Task task = taskService.createTask(3, "Задача 3", "Текст 3", "15.06.2024", 3);
        assertNotNull(task);
        assertEquals(3, task.getId());
        assertEquals("Задача 3", task.getHeading());
        assertEquals("Текст 3", task.getDescription());
        assertEquals(LocalDate.of(2024, 6, 15), task.getDateOfCompletion());
        assertEquals(3, task.getUserId());
        assertEquals(Status.NEW, task.getStatus());
    }
    @Test
    public void testEditTask() {
        Task newTask = new Task(3, "Новая задача 3", "Новый текст 3", LocalDate.now(), 1, Status.NEW);
        when(userService.findUserById(1)).thenReturn(new User(1, "Мики Маус"));

        boolean result = taskService.addTask(newTask);
        assertTrue(result);
        assertEquals(3, taskService.returnTaskList().size());
        assertEquals("Новая задача 3", taskService.returnTaskList().get(2).getHeading());
        assertEquals("Новый текст 3", taskService.returnTaskList().get(2).getDescription());
    }
    @Test
    public void testAddTask() {
        Task newTask = new Task(4, "Новая задача 3", "Новый текст 3", LocalDate.now(), 1, Status.NEW);
        when(userService.findUserById(1)).thenReturn(new User(1, "Мики Маус"));

        boolean result = taskService.addTask(newTask);
        assertTrue(result);
        assertEquals(4, taskService.returnTaskList().size());
        assertEquals("Новая задача 3", taskService.returnTaskList().get(3).getHeading());
        assertEquals("Новый текст 3", taskService.returnTaskList().get(3).getDescription());
    }

    @Test
    public void testAddTaskWithNonExistentUser() {
        Task newTask = new Task(4, "Новая задача 3", "Новый текст 3", LocalDate.now(), 99, Status.NEW);
        when(userService.findUserById(99)).thenReturn(null);

        boolean result = taskService.addTask(newTask);
        assertFalse(result);
        assertEquals(3, taskService.returnTaskList().size());
    }

    @Test
    public void testUpdateTaskStatus() {

        boolean result = taskService.updateTaskStatus(1, 2);
        assertTrue(result);
        assertEquals(2, taskService.returnTaskList().get(0).getStatus().statusNumber());
    }

    @Test
    public void testUpdateTaskStatusNonExistentTask() {

        boolean result = taskService.updateTaskStatus(99, 2);
        assertFalse(result);
    }

    @Test
    public void testFindTaskInAllTask() {

        Task task = taskService.findTaskInAllTask(1);
        assertNotNull(task);
        assertEquals(1, task.getId());

        task = taskService.findTaskInAllTask(99);
        assertNull(task);
    }
}
