package com.si.todo.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.si.todo.map.SiUserManager;
import com.si.todo.todos.SiTodo;
import com.si.todo.todos.SiTodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/todo")
public class SiTodoController {

    @Autowired
    private SiTodoService todoService;

    @Autowired
    private SiUserManager userManager;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @PostMapping("/create")
    public Map<String, Object> createTodo(@RequestBody String body) {
        Map<String, Object> result = new HashMap<>();
        try {
            JSONObject json = JSON.parseObject(body);
            String userId = json.getString("userId");
            String title = json.getString("title");
            String content = json.getString("content");
            String deadlineStr = json.getString("deadline");

            if (userId == null || title == null) {
                result.put("success", false);
                result.put("message", "用户ID和标题不能为空");
                return result;
            }

            Date deadline = null;
            if (deadlineStr != null && !deadlineStr.isEmpty()) {
                deadline = dateFormat.parse(deadlineStr);
            }

            SiTodo todo = todoService.createTodo(userId, title, content, deadline);
            result.put("success", true);
            result.put("data", todo);
        } catch (ParseException e) {
            result.put("success", false);
            result.put("message", "日期格式错误，请使用 yyyy-MM-dd HH:mm:ss 格式");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @PostMapping("/update")
    public Map<String, Object> updateTodo(@RequestBody String body) {
        Map<String, Object> result = new HashMap<>();
        try {
            JSONObject json = JSON.parseObject(body);
            String todoId = json.getString("todoId");
            String title = json.getString("title");
            String content = json.getString("content");
            String deadlineStr = json.getString("deadline");

            if (todoId == null) {
                result.put("success", false);
                result.put("message", "待办ID不能为空");
                return result;
            }

            Date deadline = null;
            if (deadlineStr != null && !deadlineStr.isEmpty()) {
                deadline = dateFormat.parse(deadlineStr);
            }

            SiTodo todo = todoService.updateTodo(todoId, title, content, deadline);
            if (todo == null) {
                result.put("success", false);
                result.put("message", "待办不存在");
            } else {
                result.put("success", true);
                result.put("data", todo);
            }
        } catch (ParseException e) {
            result.put("success", false);
            result.put("message", "日期格式错误，请使用 yyyy-MM-dd HH:mm:ss 格式");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @PostMapping("/delete")
    public Map<String, Object> deleteTodo(@RequestBody String body) {
        Map<String, Object> result = new HashMap<>();
        try {
            JSONObject json = JSON.parseObject(body);
            String todoId = json.getString("todoId");

            if (todoId == null) {
                result.put("success", false);
                result.put("message", "待办ID不能为空");
                return result;
            }

            boolean deleted = todoService.deleteTodo(todoId);
            result.put("success", deleted);
            result.put("message", deleted ? "删除成功" : "待办不存在");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @GetMapping("/list")
    public Map<String, Object> listTodos(@RequestParam String userId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<SiTodo> todos = todoService.getUserTodos(userId);
            result.put("success", true);
            result.put("data", todos);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @GetMapping("/detail")
    public Map<String, Object> getTodoDetail(@RequestParam String todoId) {
        Map<String, Object> result = new HashMap<>();
        try {
            SiTodo todo = todoService.getTodo(todoId);
            if (todo == null) {
                result.put("success", false);
                result.put("message", "待办不存在");
            } else {
                result.put("success", true);
                result.put("data", todo);
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @PostMapping("/complete")
    public Map<String, Object> completeTodo(@RequestBody String body) {
        Map<String, Object> result = new HashMap<>();
        try {
            JSONObject json = JSON.parseObject(body);
            String todoId = json.getString("todoId");

            if (todoId == null) {
                result.put("success", false);
                result.put("message", "待办ID不能为空");
                return result;
            }

            boolean completed = todoService.completeTodo(todoId);
            result.put("success", completed);
            result.put("message", completed ? "已完成" : "待办不存在");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @PostMapping("/broadcast")
    public Map<String, Object> broadcastTodo(@RequestBody String body) {
        Map<String, Object> result = new HashMap<>();
        try {
            JSONObject json = JSON.parseObject(body);
            String operatorId = json.getString("operatorId");
            String title = json.getString("title");
            String content = json.getString("content");
            String deadlineStr = json.getString("deadline");

            if (!userManager.isAdmin(operatorId)) {
                result.put("success", false);
                result.put("message", "无权限，只有管理员可以推送全员待办");
                return result;
            }

            if (title == null) {
                result.put("success", false);
                result.put("message", "标题不能为空");
                return result;
            }

            Date deadline = null;
            if (deadlineStr != null && !deadlineStr.isEmpty()) {
                deadline = dateFormat.parse(deadlineStr);
            }

            todoService.broadcastTodo(title, content, deadline);
            result.put("success", true);
            result.put("message", "全员推送成功");
        } catch (ParseException e) {
            result.put("success", false);
            result.put("message", "日期格式错误，请使用 yyyy-MM-dd HH:mm:ss 格式");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @GetMapping("/users")
    public Map<String, Object> getAllUsers() {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", userManager.getAllUsers());
        return result;
    }
}
