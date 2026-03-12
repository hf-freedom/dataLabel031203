package com.example.todoplugin.todos;

import com.example.todoplugin.map.SIUserCache;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    public List<User> getAllUsers() {
        return SIUserCache.getAllUsers();
    }

    public User getUserById(Long id) {
        return SIUserCache.getUserById(id);
    }

    public User login(String username, String password) {
        User user = SIUserCache.getUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
}
