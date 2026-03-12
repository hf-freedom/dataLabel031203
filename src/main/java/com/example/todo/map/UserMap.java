package com.example.todo.map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.example.todo.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户数据访问层
 */
@Component
public class UserMap {

    @Value("${user.config.path}")
    private Resource userConfigResource;

    private static final Map<String, User> USER_CACHE = new HashMap<>();

    @PostConstruct
    public void init() throws IOException {
        loadUsers();
    }

    private void loadUsers() throws IOException {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(userConfigResource.getInputStream(), StandardCharsets.UTF_8))) {
            String json = reader.lines().collect(Collectors.joining("\n"));
            Map<String, Object> result = JSON.parseObject(json);
            List<User> users = JSON.parseObject(JSON.toJSONString(result.get("users")), new TypeReference<List<User>>() {});
            for (User user : users) {
                USER_CACHE.put(user.getId(), user);
            }
        }
    }

    public User findById(String id) {
        return USER_CACHE.get(id);
    }

    public User findByUsername(String username) {
        return USER_CACHE.values().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    public List<User> findAll() {
        return new ArrayList<>(USER_CACHE.values());
    }

    public List<User> findAllUsers() {
        return USER_CACHE.values().stream()
                .filter(u -> !u.isAdmin())
                .collect(Collectors.toList());
    }

    public boolean validateUser(String username, String password) {
        User user = findByUsername(username);
        return user != null && user.getPassword().equals(password);
    }
}
