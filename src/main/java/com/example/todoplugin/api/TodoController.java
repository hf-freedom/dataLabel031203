package com.example.todoplugin.api;

import com.example.todoplugin.todos.Todo;
import com.example.todoplugin.todos.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @GetMapping("/user/{userId}")
    public Map<String, Object> getTodosByUserId(@PathVariable Long userId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Todo> todos = todoService.getTodosByUserId(userId);
            result.put("code", 200);
            result.put("data", todos);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", e.getMessage());
        }
        return result;
    }

    @GetMapping("/{id}")
    public Map<String, Object> getTodoById(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            Todo todo = todoService.getTodoById(id);
            result.put("code", 200);
            result.put("data", todo);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", e.getMessage());
        }
        return result;
    }

    @PostMapping
    public Map<String, Object> addTodo(@RequestBody Todo todo) {
        Map<String, Object> result = new HashMap<>();
        try {
            todoService.addTodo(todo);
            result.put("code", 200);
            result.put("msg", "添加成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", e.getMessage());
        }
        return result;
    }

    @PutMapping
    public Map<String, Object> updateTodo(@RequestBody Todo todo) {
        Map<String, Object> result = new HashMap<>();
        try {
            todoService.updateTodo(todo);
            result.put("code", 200);
            result.put("msg", "更新成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", e.getMessage());
        }
        return result;
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> deleteTodo(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            todoService.deleteTodo(id);
            result.put("code", 200);
            result.put("msg", "删除成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", e.getMessage());
        }
        return result;
    }

    @PostMapping("/push")
    public Map<String, Object> pushTodoToAll(@RequestBody Todo todo) {
        Map<String, Object> result = new HashMap<>();
        try {
            todoService.pushTodoToAll(todo);
            result.put("code", 200);
            result.put("msg", "推送成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", e.getMessage());
        }
        return result;
    }
}
