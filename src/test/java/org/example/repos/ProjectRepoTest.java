package org.example.repos;

import org.example.model.Project;
import org.example.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@DataJpaTest
public class ProjectRepoTest {
    @Autowired
    private ProjectRepo projectRepo;

    @Autowired
    private UserRepo userRepo;

    private User user1, user2;
    private Project project1, project2;
    @BeforeEach
    void setUp() {
        projectRepo.deleteAll();
        user1 = new User("Дачник 1", "login1", "123123");
        user2 = new User("Шашлык 2", "login2", "123123");

        userRepo.save(user1);
        userRepo.save(user2);

        project1 = new Project("Заголовок 1", "Описание 1");
        project2 = new Project("Заголовок 2", "Описание 2");

        project1.setUsers(Set.of(user1));
        project2.setUsers(Set.of(user1, user2));

        projectRepo.save(project1);
        projectRepo.save(project2);
    }

    @Test
    void testFindById() {
        Optional<Project> foundProject = projectRepo.findById(project1.getId());
        assertTrue(foundProject.isPresent());
        assertEquals("Заголовок 1", foundProject.get().getHeading());
    }

    @Test
    void testFindById_NonExistent() {
        Optional<Project> foundProject = projectRepo.findById(UUID.randomUUID());
        assertFalse(foundProject.isPresent());
    }

    @Test
    void testFindByUsers_Id() {
        List<Project> projects = projectRepo.findByUsers_Id(user1.getId());
        assertEquals(2, projects.size());

        projects = projectRepo.findByUsers_Id(user2.getId());
        assertEquals(1, projects.size());
        assertEquals("Заголовок 2", projects.get(0).getHeading());
    }
}
