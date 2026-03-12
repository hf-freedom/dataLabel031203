package com.example.todo.todos;

import com.example.todo.entity.User;
import com.example.todo.map.UserMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户服务层
 */
@Service
public class UserService {

    @Autowired
    private UserMap userMap;

    public User login(String username, String password) {
        if (userMap.validateUser(username, password)) {
            return userMap.findByUsername(username);
        }
        return null;
    }

    public User getUserById(String id) {
        return userMap.findById(id);
    }

    public List<User> getAllUsers() {
        return userMap.findAll();
    }

    public List<User> getAllNormalUsers() {
        return userMap.findAllUsers();
    }
}
