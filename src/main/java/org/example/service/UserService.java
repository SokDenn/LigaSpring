package org.example.service;

import org.example.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {
    private final ArrayList<User> userList = new ArrayList<>();

    public ArrayList<User> returnUserList(){
        return userList;
    }

    public User findUserById(int id) {
        for (User user : userList) {
            if (user.getId() == id) {
                return user;
            }
        } return null;
    }

}
