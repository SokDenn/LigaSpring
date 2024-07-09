package org.example.service;

import org.example.model.Project;
import org.example.model.User;
import org.example.repos.ProjectRepo;
import org.example.repos.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProjectServiceTest {
    @Mock
    private UserRepo userRepo;
    @Mock
    private ProjectRepo projectRepo;
    @InjectMocks
    private ProjectService projectService;

    private User user1;
    private Project project1;

    @BeforeEach
    public void setUp() {
        // Инициализация Mockito моков
        MockitoAnnotations.openMocks(this);

        // Инициализация тестовых данных
        user1 = new User("Дачник 1", "login1", "123123");
        user1.setId(1L);
        project1 = new Project(1L, "Заголовок", "Описание");

        Set<User> users = new HashSet<>();
        users.add(user1);
        project1.setUsers(users);
    }

    @Test
    public void testAddProject_NewProject() {
        when(projectRepo.findById(project1.getId())).thenReturn(Optional.empty());

        Boolean result = projectService.addProject(project1.getId(), project1.getHeading(), project1.getDescription());

        assertTrue(result);
        verify(projectRepo, times(1)).save(any(Project.class));
    }

    @Test
    public void testUserToProject_AddUserToProject() {
        when(projectRepo.findById(user1.getId())).thenReturn(Optional.of(project1));
        when(userRepo.findById(user1.getId())).thenReturn(Optional.of(user1));
        when(projectRepo.save(any(Project.class))).thenReturn(project1);

        Boolean result = projectService.UserToProject(1L, user1.getId(), 0);

        assertTrue(result);
        assertTrue(project1.getUsers().contains(user1));
        verify(projectRepo, times(1)).save(project1);
    }

    @Test
    public void testUserToProject_RemoveUserFromProject() {
        when(projectRepo.findById(1L)).thenReturn(Optional.of(project1));
        when(userRepo.findById(1L)).thenReturn(Optional.of(user1));

        Boolean result = projectService.UserToProject(1L, 1L, 1);

        assertTrue(result);
        assertFalse(project1.getUsers().contains(user1));
        verify(projectRepo, times(1)).save(project1);
    }

    @Test
    public void testIsUserInProject_UserInProject() {
        when(projectRepo.findById(1L)).thenReturn(Optional.of(project1));

        Boolean result = projectService.isUserInProject(1L, 1L);

        assertTrue(result);
    }

    @Test
    public void testIsUserInProject_UserNotInProject() {
        when(projectRepo.findById(1L)).thenReturn(Optional.of(project1));

        Boolean result = projectService.isUserInProject(1L, 2L);

        assertFalse(result);
    }
}
