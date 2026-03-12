package com.example.todoplugin.map;

import com.example.todoplugin.todos.Todo;
import com.example.todoplugin.todos.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SITodoCache {
    private static final String TODO_CACHE_KEY = "TODO_CACHE";
    private static long idCounter = 1;

    public static List<Todo> getAllTodos() {
        Object obj = SICache.get(TODO_CACHE_KEY);
        if (obj instanceof List) {
            return (List<Todo>) obj;
        }
        return new ArrayList<>();
    }

    public static List<Todo> getTodosByUserId(Long userId) {
        return getAllTodos().stream()
                .filter(todo -> todo.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    public static Todo getTodoById(Long id) {
        List<Todo> todos = getAllTodos();
        for (Todo todo : todos) {
            if (todo.getId().equals(id)) {
                return todo;
            }
        }
        return null;
    }

    public static void addTodo(Todo todo) {
        if (todo.getId() == null) {
            todo.setId(idCounter++);
        }
        List<Todo> todos = getAllTodos();
        todos.add(todo);
        SICache.put(TODO_CACHE_KEY, todos);
    }

    public static void updateTodo(Todo todo) {
        List<Todo> todos = getAllTodos();
        for (int i = 0; i < todos.size(); i++) {
            if (todos.get(i).getId().equals(todo.getId())) {
                todos.set(i, todo);
                break;
            }
        }
        SICache.put(TODO_CACHE_KEY, todos);
    }

    public static void deleteTodo(Long id) {
        List<Todo> todos = getAllTodos();
        todos.removeIf(todo -> todo.getId().equals(id));
        SICache.put(TODO_CACHE_KEY, todos);
    }

    public static void pushTodoToAll(Todo todo) {
        List<User> users = SIUserCache.getAllUsers();
        for (User user : users) {
            if (!"ADMIN".equals(user.getRole())) {
                Todo newTodo = new Todo();
                newTodo.setContent(todo.getContent());
                newTodo.setDeadline(todo.getDeadline());
                newTodo.setUserId(user.getId());
                newTodo.setStatus("IN_PROGRESS");
                newTodo.setNotified(false);
                addTodo(newTodo);
            }
        }
    }
}
