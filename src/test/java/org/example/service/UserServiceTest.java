package org.example.service;

import org.example.model.Project;
import org.example.model.Task;
import org.example.model.User;
import org.example.repos.ProjectRepo;
import org.example.repos.TaskRepo;
import org.example.repos.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    @Mock
    private UserRepo userRepo;
    @Mock
    private TaskRepo taskRepo;
    @Mock
    private ProjectRepo projectRepo;
    @Mock
    private ProjectService projectService;
    @InjectMocks
    private UserService userService;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testAddUserExists() {
        String name = "Дачник";
        String login = "login";
        String password = "123123";

        User existingUser = new User(name, login, password);
        when(userRepo.findByLogin(login)).thenReturn(Optional.of(existingUser));

        boolean result = userService.addUser(name, login, password);

        verify(userRepo).save(existingUser);
        assertTrue(result);
    }

    @Test
    public void testAddUserNotExist() {
        String name = "Дачник";
        String login = "login";
        String password = "123123";

        when(userRepo.findByLogin(login)).thenReturn(Optional.empty());

        boolean result = userService.addUser(name, login, password);

        verify(userRepo).save(any(User.class));
        assertTrue(result);
    }

    @Test
    public void testDeleteUser() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        List<Task> tasks = Arrays.asList(new Task(), new Task());
        List<Project> projects = Arrays.asList(new Project(), new Project());

        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(taskRepo.findByUserId(userId)).thenReturn(tasks);
        when(projectRepo.findByUsers_Id(userId)).thenReturn(projects);

        userService.deleteUser(userId);

        verify(taskRepo, times(2)).delete(any(Task.class));
        verify(projectService, times(2)).UserToProject(anyLong(), eq(userId), eq(1));
        verify(userRepo).delete(user);
    }

    @Test
    public void testDeleteUserNoTasksOrProjects() {
        Long userId = 2L;
        User user = new User();
        user.setId(userId);

        List<Task> emptyTaskList = Collections.emptyList();
        List<Project> emptyProjectList = Collections.emptyList();

        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(taskRepo.findByUserId(userId)).thenReturn(emptyTaskList);
        when(projectRepo.findByUsers_Id(userId)).thenReturn(emptyProjectList);

        userService.deleteUser(userId);

        verify(taskRepo, never()).delete(any(Task.class));
        verify(projectService, never()).UserToProject(anyLong(), eq(userId), eq(1));
        verify(userRepo).delete(user);
    }
}
