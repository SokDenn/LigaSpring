package org.example.service;

import org.example.model.Status;
import org.example.model.Task;
import org.example.model.User;
import org.example.repos.TaskRepo;
import org.example.repos.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    @InjectMocks
    private TaskService taskService;
    @Mock
    private TaskRepo taskRepo;
    @Mock
    private UserRepo userRepo;

    @Test
    public void testReturnFilteredTaskListByUser() {
        Long userId = 1L;

        taskService.returnFilteredTaskList(userId, null);

        verify(taskRepo, times(1)).findByUserId(userId);
    }

    @Test
    public void testReturnFilteredTaskListByStatusNumber() {
        Integer statusNumber = 1;
        Status status = Status.numberOfStatus(statusNumber);

        taskService.returnFilteredTaskList(null, statusNumber);

        verify(taskRepo, times(1)).findByStatus(status);
    }

    @Test
    public void testReturnFilteredTaskListByStatusNumberAndUser() {
        Long userId = 1L;
        Integer statusNumber = 1;
        Status status = Status.numberOfStatus(statusNumber);

        taskService.returnFilteredTaskList(userId, statusNumber);

        verify(taskRepo, times(1)).findByUserIdAndStatus(userId, status);
    }

    @Test
    public void testCreateTask() {
        int taskId = 1;
        String heading = "Задача 1";
        String description = "Текст 1";
        String dateOfCompletionStr = "19.06.2024";
        User user = new User();
        LocalDate dateOfCompletion = LocalDate.of(2024, 6, 19);

        Task task = taskService.createTask(taskId, heading, description, dateOfCompletionStr, user);

        assertNotNull(task);
        assertEquals(taskId, task.getId());
        assertEquals(heading, task.getHeading());
        assertEquals(description, task.getDescription());
        assertEquals(dateOfCompletion, task.getDateOfCompletion());
        assertEquals(user, task.getUser());
        assertEquals(Status.NEW, task.getStatus());
    }
    @Test
    public void testEditTask() {
        User user = new User();
        user.setId(1L);
        Task existingTask = new Task(1, "Задача до", "Текст до", LocalDate.now(), user, Status.NEW);
        Task newTask = new Task(1, "Задача после", "Текст после", LocalDate.now(), user, Status.NEW);

        when(userRepo.existsById(user.getId())).thenReturn(true);
        when(taskRepo.findById((long) newTask.getId())).thenReturn(Optional.of(existingTask));

        Boolean result = taskService.addTask(newTask);

        assertTrue(result);
        verify(taskRepo, times(1)).save(existingTask);
        assertEquals("Задача после", existingTask.getHeading());
        assertEquals("Текст после", existingTask.getDescription());
    }
    @Test
    public void testAddTask() {
        User user = new User();
        user.setId(1L);
        Task task = new Task(1, "Задача", "Текст", LocalDate.now(), user, Status.NEW);

        when(userRepo.existsById(user.getId())).thenReturn(true);
        when(taskRepo.findById((long) task.getId())).thenReturn(Optional.empty());

        Boolean result = taskService.addTask(task);

        assertTrue(result);
        verify(taskRepo, times(1)).save(task);
        assertEquals("Задача", task.getHeading());
        assertEquals("Текст", task.getDescription());
    }

    @Test
    public void testUpdateTaskStatus() {
        Integer taskId = 1;
        Integer statusNumber = 1;
        Task task = new Task();
        task.setId(taskId);
        task.setStatus(Status.NEW);

        when(taskRepo.findById((long) taskId)).thenReturn(Optional.of(task));

        Boolean result = taskService.updateTaskStatus(taskId, statusNumber);

        assertTrue(result);
        verify(taskRepo, times(1)).save(any(Task.class));
    }

    @Test
    public void testUpdateTaskStatusNonExistentTask() {
        Integer taskId = 1;
        Integer statusNumber = 1;
        Task task = new Task();
        task.setId(taskId);
        task.setStatus(Status.NEW);
        Integer nonExistentTaskId = 99;

        when(taskRepo.findById((long) nonExistentTaskId)).thenReturn(Optional.empty());

        Boolean result = taskService.updateTaskStatus(nonExistentTaskId, statusNumber);

        assertFalse(result);
        verify(taskRepo, never()).save(any(Task.class));
    }
}
