package org.example.repos;

import org.example.model.Status;
import org.example.model.Task;
import org.example.model.User;
import org.example.repos.TaskRepo;
import org.example.repos.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class UserRepoTest {
    @Autowired
    private UserRepo userRepo;
    private User user1, user2;

    @BeforeEach
    void setUp() {
        user1 = new User("Дачник 1");
        user2 = new User("Дачник 2");

        userRepo.save(user1);
        userRepo.save(user2);
    }

    @Test
    void testFindById() {
        Optional<User> foundUser1 = userRepo.findById(user1.getId());
        Optional<User> foundUser2 = userRepo.findById(user2.getId());

        assertAll(
                () -> assertEquals("Дачник 1", foundUser1.get().getName()),
                () -> assertTrue(foundUser1.isPresent()),

                () -> assertEquals("Дачник 2", foundUser2.get().getName()),
                () -> assertTrue(foundUser2.isPresent())
        );
    }

    @Test
    void testFindAll() {
        Iterable<User> users = userRepo.findAll();

        assertNotNull(users);
        assertTrue(((List<User>) users).size() == 2);
    }
}