package com.example.todo.api;

import com.example.todo.entity.Todo;
import com.example.todo.entity.User;
import com.example.todo.todos.TodoService;
import com.example.todo.todos.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 待办事项API控制器
 */
@RestController
@RequestMapping("/api/todo")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<?> createTodo(@RequestBody Todo todo, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).body("未登录");
        }
        Todo created = todoService.createTodo(todo, user.getId());
        return ResponseEntity.ok(success(created));
    }

    @PostMapping("/admin/create")
    public ResponseEntity<?> createTodoByAdmin(@RequestBody Map<String, Object> params, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).body("未登录");
        }
        if (!user.isAdmin()) {
            return ResponseEntity.status(403).body("无权限");
        }
        String targetUserId = (String) params.get("targetUserId");
        Map<String, Object> todoData = (Map<String, Object>) params.get("todo");
        Todo todo = mapToTodo(todoData);
        Todo created = todoService.createTodoByAdmin(todo, user.getId(), targetUserId);
        return ResponseEntity.ok(success(created));
    }

    @PostMapping("/admin/push-all")
    public ResponseEntity<?> pushTodoToAll(@RequestBody Map<String, Object> todoData, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).body("未登录");
        }
        if (!user.isAdmin()) {
            return ResponseEntity.status(403).body("无权限");
        }
        Todo todo = mapToTodo(todoData);
        todoService.pushTodoToAllUsers(todo, user.getId());
        return ResponseEntity.ok(success("推送成功"));
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateTodo(@RequestBody Todo todo, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).body("未登录");
        }
        try {
            Todo updated = todoService.updateTodo(todo, user.getId());
            return ResponseEntity.ok(success(updated));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(error(e.getMessage()));
        }
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<?> deleteTodo(@PathVariable String id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).body("未登录");
        }
        try {
            todoService.deleteTodo(id, user.getId());
            return ResponseEntity.ok(success("删除成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(error(e.getMessage()));
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getTodo(@PathVariable String id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).body("未登录");
        }
        try {
            Todo todo = todoService.getTodo(id, user.getId());
            return ResponseEntity.ok(success(todo));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(error(e.getMessage()));
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> listTodos(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).body("未登录");
        }
        List<Todo> todos = todoService.getUserTodos(user.getId());
        return ResponseEntity.ok(success(todos));
    }

    @GetMapping("/list-by-status/{status}")
    public ResponseEntity<?> listTodosByStatus(@PathVariable String status, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).body("未登录");
        }
        List<Todo> todos = todoService.getUserTodosByStatus(user.getId(), status);
        return ResponseEntity.ok(success(todos));
    }

    @GetMapping("/admin/list-all")
    public ResponseEntity<?> listAllTodos(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).body("未登录");
        }
        try {
            List<Todo> todos = todoService.getAllTodos(user.getId());
            return ResponseEntity.ok(success(todos));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(error(e.getMessage()));
        }
    }

    @PostMapping("/update-status")
    public ResponseEntity<?> updateStatus(@RequestBody Map<String, String> params, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).body("未登录");
        }
        try {
            Todo todo = todoService.updateStatus(params.get("id"), params.get("status"), user.getId());
            return ResponseEntity.ok(success(todo));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(error(e.getMessage()));
        }
    }

    private Todo mapToTodo(Map<String, Object> data) {
        Todo todo = new Todo();
        todo.setTitle((String) data.get("title"));
        todo.setContent((String) data.get("content"));
        if (data.get("deadline") != null) {
            String deadlineStr = (String) data.get("deadline");
            todo.setDeadline(parseDeadline(deadlineStr));
        }
        return todo;
    }

    private LocalDateTime parseDeadline(String deadlineStr) {
        if (deadlineStr == null || deadlineStr.isEmpty()) {
            return null;
        }
        deadlineStr = deadlineStr.replace(" ", "T");
        if (deadlineStr.length() == 16) {
            deadlineStr += ":00";
        }
        return LocalDateTime.parse(deadlineStr);
    }

    private Map<String, Object> success(Object data) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("success", true);
        result.put("data", data);
        return result;
    }

    private Map<String, Object> error(String message) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        result.put("success", false);
        result.put("message", message);
        return result;
    }
}
