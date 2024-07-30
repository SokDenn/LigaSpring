package org.example.service;

import org.example.dto.TaskDTO;
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
import java.util.UUID;

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
        UUID userId = UUID.randomUUID();

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
        UUID userId = UUID.randomUUID();
        Integer statusNumber = 1;
        Status status = Status.numberOfStatus(statusNumber);

        taskService.returnFilteredTaskList(userId, statusNumber);

        verify(taskRepo, times(1)).findByUserIdAndStatus(userId, status);
    }

    @Test
    public void testAddTaskSuccess() {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setDateOfCompletionStr("29.07.2024");
        taskDTO.setUserId(UUID.randomUUID());
        taskDTO.setProjectId(UUID.randomUUID());

        when(userRepo.findById(taskDTO.getUserId())).thenReturn(Optional.of(new User()));
        when(projectRepo.findById(taskDTO.getProjectId())).thenReturn(Optional.of(new Project()));

        boolean result = taskService.addTask(taskDTO);

        assertTrue(result);
        verify(taskRepo, times(1)).save(any(Task.class));
    }
    @Test
    public void testAddTaskProjectNotFound() {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setDateOfCompletionStr("29.07.2024");
        taskDTO.setUserId(UUID.randomUUID());
        taskDTO.setProjectId(UUID.randomUUID());

        when(userRepo.findById(taskDTO.getUserId())).thenReturn(Optional.of(new User()));
        when(projectRepo.findById(taskDTO.getProjectId())).thenReturn(Optional.empty());

        boolean result = taskService.addTask(taskDTO);

        assertFalse(result);
        verify(taskRepo, times(0)).save(any(Task.class));
    }

    @Test
    public void testAddTaskDateParsingError() {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setDateOfCompletionStr("Ошибка парсинга даты");
        taskDTO.setUserId(UUID.randomUUID());
        taskDTO.setProjectId(UUID.randomUUID());

        boolean result = taskService.addTask(taskDTO);

        assertFalse(result);
        verify(taskRepo, times(0)).save(any(Task.class));
    }

    @Test
    public void testUpdateTaskStatus() {
        UUID taskId = UUID.randomUUID();
        Integer newStatusNumber = 2;
        Status newStatus = Status.numberOfStatus(newStatusNumber);
        Task task = new Task();
        task.setStatus(Status.NEW);

        when(taskRepo.findById(taskId)).thenReturn(Optional.of(task));

        Boolean result = taskService.updateTaskStatus(taskId, newStatusNumber);

        assertTrue(result);
        assertEquals(newStatus, task.getStatus());
        verify(taskRepo, times(1)).save(task);
    }

    @Test
    public void testUpdateTaskStatusNonExistentTask() {
        UUID taskId = UUID.randomUUID();

        when(taskRepo.findById(taskId)).thenReturn(Optional.empty());

        Boolean result = taskService.updateTaskStatus(taskId, 1);

        assertFalse(result);
        verify(taskRepo, never()).save(any());
    }
}
