package org.example.repos;

import jakarta.transaction.Transactional;
import org.example.model.Project;
import org.example.model.Task;
import org.example.model.User;
import org.example.repos.ProjectRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class UserRepoTest {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ProjectRepo projectRepo;
    @Autowired
    private TaskRepo taskRepo;
    private User user1, user2;

    @BeforeEach
    void setUp() {
        for(Project project : projectRepo.findAll()){
            project.getUsers().clear();
        }
        taskRepo.deleteAll();
        userRepo.deleteAll();

        user1 = new User("Дачник 1", "login1", "123123");
        user2 = new User("Дачник 2", "login2", "123123");

        userRepo.save(user1);
        userRepo.save(user2);
    }

    @Test
    void testFindById() {
        Optional<User> foundUser1 = userRepo.findById(user1.getId());
        Optional<User> foundUser2 = userRepo.findById(user2.getId());

        assertAll(
                () -> assertTrue(foundUser1.isPresent(), "Пользователь 1 должен быть найден"),
                () -> assertEquals("Дачник 1", foundUser1.get().getName(), "Имя пользователя 1 должно быть 'Дачник 1'"),
                () -> assertTrue(foundUser2.isPresent(), "Пользователь 2 должен быть найден"),
                () -> assertEquals("Дачник 2", foundUser2.get().getName(), "Имя пользователя 2 должно быть 'Дачник 2'")
        );
    }

    @Test
    void testFindByUsername() {
        Optional<User> foundUser1 = userRepo.findByUsername(user1.getUsername());
        Optional<User> foundUser2 = userRepo.findByUsername(user2.getUsername());

        assertAll(
                () -> assertTrue(foundUser1.isPresent(), "Пользователь 1 должен быть найден по имени пользователя"),
                () -> assertEquals("Дачник 1", foundUser1.get().getName(), "Имя пользователя 1 должно быть 'Дачник 1'"),
                () -> assertTrue(foundUser2.isPresent(), "Пользователь 2 должен быть найден по имени пользователя"),
                () -> assertEquals("Дачник 2", foundUser2.get().getName(), "Имя пользователя 2 должно быть 'Дачник 2'")
        );
    }

    @Test
    void testFindAll() {
        Iterable<User> usersIterable = userRepo.findAll();
        List<User> users = StreamSupport.stream(usersIterable.spliterator(), false)
                .collect(Collectors.toList());

        assertAll(
                () -> assertNotNull(users, "Список пользователей не должен быть пустым"),
                () -> assertEquals(2, users.size(), "В списке должно быть 2 пользователя")
        );
    }

    @Test
    void testSave() {
        User user3 = new User("Дачник 3", "login3", "password3");
        userRepo.save(user3);

        Optional<User> foundUser3 = userRepo.findById(user3.getId());
        assertAll(
                () -> assertTrue(foundUser3.isPresent(), "Пользователь 3 должен быть сохранен и найден"),
                () -> assertEquals("Дачник 3", foundUser3.get().getName(), "Имя пользователя 3 должно быть 'Дачник 3'")
        );
    }

    @Test
    void testDelete() {
        userRepo.delete(user1);
        Optional<User> foundUser1 = userRepo.findById(user1.getId());

        assertFalse(foundUser1.isPresent(), "Пользователь 1 должен быть удален");
    }
}