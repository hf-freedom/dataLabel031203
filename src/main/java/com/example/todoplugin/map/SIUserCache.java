package com.example.todoplugin.map;

import com.example.todoplugin.todos.User;

import java.util.ArrayList;
import java.util.List;

public class SIUserCache {
    private static final String USER_CACHE_KEY = "USER_CACHE";
    private static long idCounter = 1;

    public static void initUsers(List<User> users) {
        SICache.put(USER_CACHE_KEY, new ArrayList<>(users));
        for (User user : users) {
            if (user.getId() >= idCounter) {
                idCounter = user.getId() + 1;
            }
        }
    }

    public static List<User> getAllUsers() {
        Object obj = SICache.get(USER_CACHE_KEY);
        if (obj instanceof List) {
            return (List<User>) obj;
        }
        return new ArrayList<>();
    }

    public static User getUserById(Long id) {
        List<User> users = getAllUsers();
        for (User user : users) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }

    public static User getUserByUsername(String username) {
        List<User> users = getAllUsers();
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public static void addUser(User user) {
        if (user.getId() == null) {
            user.setId(idCounter++);
        }
        List<User> users = getAllUsers();
        users.add(user);
        SICache.put(USER_CACHE_KEY, users);
    }

    public static void updateUser(User user) {
        List<User> users = getAllUsers();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(user.getId())) {
                users.set(i, user);
                break;
            }
        }
        SICache.put(USER_CACHE_KEY, users);
    }

    public static void deleteUser(Long id) {
        List<User> users = getAllUsers();
        users.removeIf(user -> user.getId().equals(id));
        SICache.put(USER_CACHE_KEY, users);
    }
}
