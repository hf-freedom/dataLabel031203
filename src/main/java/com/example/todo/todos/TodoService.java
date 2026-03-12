package com.example.todo.todos;

import com.example.todo.entity.Todo;
import com.example.todo.entity.User;
import com.example.todo.map.TodoMap;
import com.example.todo.map.UserMap;
import com.example.todo.util.SISendUtil;
import com.example.todo.util.SITimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 待办事项服务层
 */
@Service
public class TodoService {

    @Autowired
    private TodoMap todoMap;

    @Autowired
    private UserMap userMap;

    public Todo createTodo(Todo todo, String userId) {
        todo.setUserId(userId);
        todo.setCreateBy(userId);
        todo.setStatus(Todo.STATUS_PENDING);
        return todoMap.save(todo);
    }

    public Todo createTodoByAdmin(Todo todo, String adminId, String targetUserId) {
        User admin = userMap.findById(adminId);
        if (admin == null || !admin.isAdmin()) {
            throw new RuntimeException("无权限操作");
        }
        todo.setUserId(targetUserId);
        todo.setCreateBy(adminId);
        todo.setStatus(Todo.STATUS_PENDING);
        return todoMap.save(todo);
    }

    public void pushTodoToAllUsers(Todo todoTemplate, String adminId) {
        User admin = userMap.findById(adminId);
        if (admin == null || !admin.isAdmin()) {
            throw new RuntimeException("无权限操作");
        }
        List<User> users = userMap.findAllUsers();
        for (User user : users) {
            Todo todo = new Todo();
            todo.setTitle(todoTemplate.getTitle());
            todo.setContent(todoTemplate.getContent());
            todo.setDeadline(todoTemplate.getDeadline());
            todo.setUserId(user.getId());
            todo.setCreateBy(adminId);
            todo.setStatus(Todo.STATUS_PENDING);
            todoMap.save(todo);
        }
    }

    public Todo updateTodo(Todo todo, String userId) {
        Todo existing = todoMap.findById(todo.getId());
        if (existing == null) {
            throw new RuntimeException("待办事项不存在");
        }
        User user = userMap.findById(userId);
        if (!existing.getUserId().equals(userId) && !user.isAdmin()) {
            throw new RuntimeException("无权限修改此待办事项");
        }
        existing.setTitle(todo.getTitle());
        existing.setContent(todo.getContent());
        existing.setDeadline(todo.getDeadline());
        existing.setUpdateTime(LocalDateTime.now());
        return todoMap.save(existing);
    }

    public void deleteTodo(String id, String userId) {
        Todo existing = todoMap.findById(id);
        if (existing == null) {
            throw new RuntimeException("待办事项不存在");
        }
        User user = userMap.findById(userId);
        if (!existing.getUserId().equals(userId) && !user.isAdmin()) {
            throw new RuntimeException("无权限删除此待办事项");
        }
        todoMap.deleteById(id);
    }

    public Todo getTodo(String id, String userId) {
        Todo todo = todoMap.findById(id);
        if (todo == null) {
            return null;
        }
        User user = userMap.findById(userId);
        if (!todo.getUserId().equals(userId) && !user.isAdmin()) {
            throw new RuntimeException("无权限查看此待办事项");
        }
        return todo;
    }

    public List<Todo> getUserTodos(String userId) {
        return todoMap.findByUserId(userId);
    }

    public List<Todo> getUserTodosByStatus(String userId, String status) {
        return todoMap.findByUserIdAndStatus(userId, status);
    }

    public List<Todo> getAllTodos(String userId) {
        User user = userMap.findById(userId);
        if (user == null || !user.isAdmin()) {
            throw new RuntimeException("无权限查看所有待办事项");
        }
        return todoMap.findAll();
    }

    public Todo updateStatus(String id, String status, String userId) {
        Todo existing = todoMap.findById(id);
        if (existing == null) {
            throw new RuntimeException("待办事项不存在");
        }
        User user = userMap.findById(userId);
        if (!existing.getUserId().equals(userId) && !user.isAdmin()) {
            throw new RuntimeException("无权限修改此待办事项");
        }
        existing.setStatus(status);
        existing.setUpdateTime(LocalDateTime.now());
        return todoMap.save(existing);
    }

    public List<Todo> checkAndUpdateOverdue() {
        List<Todo> allTodos = todoMap.findAll();
        List<Todo> overdueTodos = allTodos.stream()
                .filter(t -> !Todo.STATUS_COMPLETED.equals(t.getStatus())
                        && !Todo.STATUS_OVERDUE.equals(t.getStatus())
                        && t.isOverdue())
                .collect(Collectors.toList());

        for (Todo todo : overdueTodos) {
            todo.setStatus(Todo.STATUS_OVERDUE);
            todo.setUpdateTime(LocalDateTime.now());
            todoMap.save(todo);
        }
        return overdueTodos;
    }

    public List<Todo> getNearDeadlineTodos() {
        return todoMap.findAll().stream()
                .filter(t -> Todo.STATUS_PENDING.equals(t.getStatus())
                        || Todo.STATUS_IN_PROGRESS.equals(t.getStatus()))
                .filter(Todo::isNearDeadline)
                .collect(Collectors.toList());
    }

    public void markReminded(String todoId) {
        Todo todo = todoMap.findById(todoId);
        if (todo != null) {
            todo.setReminded(true);
            todoMap.save(todo);
        }
    }
}
