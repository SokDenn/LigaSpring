package org.example.service;

import org.example.model.*;
import org.example.repos.ProjectRepo;
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
    @Mock
    private ProjectRepo projectRepo;

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
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTaskId(1L);
        taskDTO.setDateOfCompletionStr("01.01.2024");
        taskDTO.setUserId(1L);
        taskDTO.setProjectId(1L);
        User user = new User();
        Project project = new Project();

        when(userRepo.findById(taskDTO.getUserId())).thenReturn(Optional.of(user));
        when(projectRepo.findById(taskDTO.getProjectId())).thenReturn(Optional.of(project));

        Task task = taskService.createTask(taskDTO);

        assertNotNull(task);
        assertEquals(LocalDate.of(2024, 1, 1), task.getDateOfCompletion());
        assertEquals(user, task.getUser());
        assertEquals(project, task.getProject());
    }
    @Test
    public void testEditTask() {
        User user = new User();
        user.setId(1L);

        Project project = new Project();
        project.setId(1L);

        Task existingTask = new Task();
        existingTask.setId(1L);
        Task updatedTask = new Task();

        updatedTask.setId(1L);
        updatedTask.setHeading("Задача после");
        updatedTask.setDescription("Текст после");
        updatedTask.setDateOfCompletion(existingTask.getDateOfCompletion());
        updatedTask.setUser(user);
        updatedTask.setStatus(Status.IN_WORK);
        updatedTask.setProject(project);

        when(userRepo.existsById(user.getId())).thenReturn(true);
        when(projectRepo.existsById(project.getId())).thenReturn(true);
        when(projectRepo.findById(project.getId())).thenReturn(Optional.of(project));
        when(taskRepo.findById(existingTask.getId())).thenReturn(Optional.of(existingTask));

        Boolean result = taskService.addTask(updatedTask);

        assertTrue(result);
        verify(taskRepo, times(1)).save(existingTask);
        assertEquals("Задача после", existingTask.getHeading());
        assertEquals("Текст после", existingTask.getDescription());
        assertEquals(Status.IN_WORK, existingTask.getStatus());
    }
    @Test
    public void testAddTask() {
        User user = new User();
        user.setId(1L);

        Project project = new Project();
        project.setId(1L);

        Task task = new Task();
        task.setId(1L);
        task.setUser(user);
        task.setProject(project);

        when(userRepo.existsById(user.getId())).thenReturn(true);
        when(projectRepo.existsById(project.getId())).thenReturn(true);
        when(projectRepo.findById(project.getId())).thenReturn(Optional.of(project));
        when(taskRepo.findById(task.getId())).thenReturn(Optional.empty());

        Boolean result = taskService.addTask(task);

        verify(taskRepo, times(1)).save(task);
        assertTrue(result);
    }

    @Test
    public void testUpdateTaskStatus() {
        Long taskId = 1L;
        Integer newStatusNumber = 2;
        Status newStatus = Status.numberOfStatus(newStatusNumber);
        Task task = new Task();
        task.setId(taskId);
        task.setStatus(Status.NEW);

        when(taskRepo.findById(taskId)).thenReturn(Optional.of(task));

        Boolean result = taskService.updateTaskStatus(taskId, newStatusNumber);

        assertTrue(result);
        assertEquals(newStatus, task.getStatus());
        verify(taskRepo, times(1)).save(task);
    }

    @Test
    public void testUpdateTaskStatusNonExistentTask() {
        Long taskId = 999L;

        when(taskRepo.findById(taskId)).thenReturn(Optional.empty());

        Boolean result = taskService.updateTaskStatus(taskId, 1);

        assertFalse(result);
        verify(taskRepo, never()).save(any());
    }
}
