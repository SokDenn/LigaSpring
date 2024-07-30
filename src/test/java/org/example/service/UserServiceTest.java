package org.example.service;

import org.example.model.Project;
import org.example.model.Role;
import org.example.model.Task;
import org.example.model.User;
import org.example.repos.ProjectRepo;
import org.example.repos.RoleRepo;
import org.example.repos.TaskRepo;
import org.example.repos.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    @Mock
    private UserRepo userRepo;
    @Mock
    private TaskRepo taskRepo;
    @Mock
    private RoleRepo roleRepo;
    @Mock
    private PasswordEncoder passwordEncoder;
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
        when(userRepo.findByUsername(login)).thenReturn(Optional.of(existingUser));
        when(userRepo.findById(any())).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode(any())).thenReturn("123");

        boolean result = userService.addUser(UUID.randomUUID(), name, login, password);

        verify(userRepo).save(existingUser);
        assertTrue(result);
    }

    @Test
    public void testAddUserNotExist() {
        String name = "Дачник";
        String login = "login";
        String password = "123123";

        when(userRepo.findByUsername(login)).thenReturn(Optional.empty());
        when(roleRepo.findByName("USER")).thenReturn(Optional.of(new Role("USER")));
        when(passwordEncoder.encode(any())).thenReturn("123");

        boolean result = userService.addUser(null, name, login, password);

        verify(userRepo).save(any(User.class));
        assertTrue(result);
    }

    @Test
    public void testDeleteUser() {
        User user = new User();

        List<Task> tasks = Arrays.asList(new Task(), new Task());
        List<Project> projects = Arrays.asList(new Project(), new Project());

        when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));
        when(taskRepo.findByUserId(user.getId())).thenReturn(tasks);
        when(projectRepo.findByUsersId(user.getId())).thenReturn(projects);

        userService.deleteUser(user.getId());

        verify(taskRepo, times(2)).delete(any(Task.class));
        verify(projectService, times(2)).UserToProject(any(), eq(user.getId()), eq(1));
        verify(userRepo).delete(user);
    }

    @Test
    public void testDeleteUserNoTasksOrProjects() {
        User user = new User();

        List<Task> emptyTaskList = Collections.emptyList();
        List<Project> emptyProjectList = Collections.emptyList();

        when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));
        when(taskRepo.findByUserId(user.getId())).thenReturn(emptyTaskList);
        when(projectRepo.findByUsersId(user.getId())).thenReturn(emptyProjectList);

        userService.deleteUser(user.getId());

        verify(taskRepo, never()).delete(any(Task.class));
        verify(projectService, never()).UserToProject(any(), eq(user.getId()), eq(1));
        verify(userRepo).delete(user);
    }
}
