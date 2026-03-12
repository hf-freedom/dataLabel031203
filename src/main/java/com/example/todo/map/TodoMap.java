package com.example.todo.map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.example.todo.entity.Todo;
import com.example.todo.util.SICacheUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 待办事项数据访问层
 */
@Component
public class TodoMap {

    private static final String TODO_CACHE_KEY = "TODO_LIST";

    public Todo save(Todo todo) {
        List<Todo> todos = findAll();
        if (todo.getId() == null) {
            todo.setId(UUID.randomUUID().toString());
            todos.add(todo);
        } else {
            for (int i = 0; i < todos.size(); i++) {
                if (todos.get(i).getId().equals(todo.getId())) {
                    todos.set(i, todo);
                    break;
                }
            }
        }
        saveAll(todos);
        return todo;
    }

    public void deleteById(String id) {
        List<Todo> todos = findAll();
        todos = todos.stream()
                .filter(t -> !t.getId().equals(id))
                .collect(Collectors.toList());
        saveAll(todos);
    }

    public Todo findById(String id) {
        return findAll().stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Todo> findAll() {
        String json = SICacheUtil.get(TODO_CACHE_KEY, String.class);
        if (json == null) {
            return new ArrayList<>();
        }
        return JSON.parseObject(json, new TypeReference<List<Todo>>() {});
    }

    public List<Todo> findByUserId(String userId) {
        return findAll().stream()
                .filter(t -> t.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    public List<Todo> findByStatus(String status) {
        return findAll().stream()
                .filter(t -> t.getStatus().equals(status))
                .collect(Collectors.toList());
    }

    public List<Todo> findByUserIdAndStatus(String userId, String status) {
        return findAll().stream()
                .filter(t -> t.getUserId().equals(userId) && t.getStatus().equals(status))
                .collect(Collectors.toList());
    }

    public void saveAll(List<Todo> todos) {
        SICacheUtil.put(TODO_CACHE_KEY, JSON.toJSONString(todos));
    }

    public void deleteAll() {
        SICacheUtil.remove(TODO_CACHE_KEY);
    }
}
