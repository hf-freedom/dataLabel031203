package com.si.todo.map;

import com.si.todo.todos.SiTodo;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SiCacheManager {

    private final Map<String, SiTodo> todoCache = new ConcurrentHashMap<>();

    public void saveTodo(SiTodo todo) {
        if (todo != null && todo.getId() != null) {
            todoCache.put(todo.getId(), todo);
        }
    }

    public SiTodo getTodo(String todoId) {
        return todoCache.get(todoId);
    }

    public boolean deleteTodo(String todoId) {
        return todoCache.remove(todoId) != null;
    }

    public List<SiTodo> getAllTodos() {
        return new ArrayList<>(todoCache.values());
    }

    public List<SiTodo> getTodosByUserId(String userId) {
        List<SiTodo> result = new ArrayList<>();
        for (SiTodo todo : todoCache.values()) {
            if (userId.equals(todo.getUserId())) {
                result.add(todo);
            }
        }
        return result;
    }
}
