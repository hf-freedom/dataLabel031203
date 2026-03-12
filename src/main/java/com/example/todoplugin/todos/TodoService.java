package com.example.todoplugin.todos;

import com.example.todoplugin.map.SITodoCache;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TodoService {

    public List<Todo> getTodosByUserId(Long userId) {
        updateTodoStatus();
        return SITodoCache.getTodosByUserId(userId);
    }

    public Todo getTodoById(Long id) {
        updateTodoStatus();
        return SITodoCache.getTodoById(id);
    }

    public void addTodo(Todo todo) {
        if (todo.getStatus() == null) {
            todo.setStatus("IN_PROGRESS");
        }
        todo.setNotified(false);
        SITodoCache.addTodo(todo);
    }

    public void updateTodo(Todo todo) {
        SITodoCache.updateTodo(todo);
    }

    public void deleteTodo(Long id) {
        SITodoCache.deleteTodo(id);
    }

    public void pushTodoToAll(Todo todo) {
        SITodoCache.pushTodoToAll(todo);
    }

    private void updateTodoStatus() {
        List<Todo> todos = SITodoCache.getAllTodos();
        Date now = new Date();
        for (Todo todo : todos) {
            if ("IN_PROGRESS".equals(todo.getStatus()) && todo.getDeadline() != null) {
                if (now.after(todo.getDeadline())) {
                    todo.setStatus("OVERDUE");
                    SITodoCache.updateTodo(todo);
                }
            }
        }
    }
}
