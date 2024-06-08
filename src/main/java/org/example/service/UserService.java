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

    public void addUser(String userName) {
        if(userName == null || userName.isEmpty()) {
            System.out.println("Пользователь не добавлен! Пустое имя");
        }
        userList.add(new User(generateIdUser(), userName));
    }

    public User findUserById(int id) {
        for (User user : userList) {
            if (user.getId() == id) {
                return user;
            }
        } return null;
    }

    public int generateIdUser(){
        Set<Integer> uniqueId = new HashSet<>();
        int newId = 0;

        if(!userList.isEmpty()) {
            for (User user : userList) {
                uniqueId.add(user.getId());
            }

            while (true) {
                if (uniqueId.add(newId)) {
                    return newId;
                }
                newId++;
            }
        }
        return newId;
    }
}
