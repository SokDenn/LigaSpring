package org.example.service;

import org.example.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {
    private UserService userService;

    @BeforeEach
    public void setUp(){
        userService = new UserService();

        userService.returnUserList().add(new User(1, "Майкл"));
        userService.returnUserList().add(new User(2, "Джек"));
        userService.returnUserList().add(new User(3, "Николас"));
    }
    @Test
    public void testFindUserByIdExists(){
        User user = userService.findUserById(2);
        assertNotNull(user);
        assertEquals(2, user.getId());
        assertEquals("Джек", user.getName());
    }

    @Test
    public void testFindUserByIdDoesNotExist(){
        User user = userService.findUserById(4);
        assertNull(user);
    }
}
