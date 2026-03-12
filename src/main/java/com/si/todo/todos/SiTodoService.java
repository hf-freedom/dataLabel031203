package com.si.todo.todos;

import com.si.todo.map.SiCacheManager;
import com.si.todo.map.SiUserManager;
import com.si.todo.util.SISendUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SiTodoService {

    @Autowired
    private SiCacheManager cacheManager;

    @Autowired
    private SiUserManager userManager;

    public SiTodo createTodo(String userId, String title, String content, Date deadline) {
        SiTodo todo = new SiTodo();
        todo.setId(UUID.randomUUID().toString());
        todo.setUserId(userId);
        todo.setTitle(title);
        todo.setContent(content);
        todo.setDeadline(deadline);
        cacheManager.saveTodo(todo);
        return todo;
    }

    public SiTodo updateTodo(String todoId, String title, String content, Date deadline) {
        SiTodo todo = cacheManager.getTodo(todoId);
        if (todo == null) {
            return null;
        }
        if (title != null) {
            todo.setTitle(title);
        }
        if (content != null) {
            todo.setContent(content);
        }
        if (deadline != null) {
            todo.setDeadline(deadline);
        }
        todo.setUpdateTime(new Date());
        cacheManager.saveTodo(todo);
        return todo;
    }

    public boolean deleteTodo(String todoId) {
        return cacheManager.deleteTodo(todoId);
    }

    public SiTodo getTodo(String todoId) {
        return cacheManager.getTodo(todoId);
    }

    public List<SiTodo> getUserTodos(String userId) {
        return cacheManager.getAllTodos().stream()
                .filter(todo -> todo.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    public List<SiTodo> getAllTodos() {
        return new ArrayList<>(cacheManager.getAllTodos());
    }

    public boolean completeTodo(String todoId) {
        SiTodo todo = cacheManager.getTodo(todoId);
        if (todo == null) {
            return false;
        }
        todo.setStatus(SiTodoStatus.COMPLETED);
        todo.setUpdateTime(new Date());
        cacheManager.saveTodo(todo);
        return true;
    }

    public void broadcastTodo(String title, String content, Date deadline) {
        List<String> allUserIds = userManager.getAllUserIds();
        for (String userId : allUserIds) {
            SiTodo todo = new SiTodo();
            todo.setId(UUID.randomUUID().toString());
            todo.setUserId(userId);
            todo.setTitle(title);
            todo.setContent(content);
            todo.setDeadline(deadline);
            todo.setBroadcast(true);
            cacheManager.saveTodo(todo);
        }
    }

    @Scheduled(fixedRate = 60000)
    public void checkTodoStatus() {
        Date now = new Date();
        List<SiTodo> allTodos = cacheManager.getAllTodos();
        
        for (SiTodo todo : allTodos) {
            if (todo.getStatus() == SiTodoStatus.COMPLETED) {
                continue;
            }
            
            if (todo.getDeadline() != null && now.after(todo.getDeadline())) {
                todo.setStatus(SiTodoStatus.OVERDUE);
                cacheManager.saveTodo(todo);
            }
        }
    }

    @Scheduled(fixedRate = 60000)
    public void checkReminder() {
        Date now = new Date();
        long thirtyMinutes = 30 * 60 * 1000;
        
        List<SiTodo> allTodos = cacheManager.getAllTodos();
        
        for (SiTodo todo : allTodos) {
            if (todo.getStatus() == SiTodoStatus.COMPLETED) {
                continue;
            }
            if (todo.isReminded()) {
                continue;
            }
            if (todo.getDeadline() == null) {
                continue;
            }
            
            long timeDiff = todo.getDeadline().getTime() - now.getTime();
            
            if (timeDiff > 0 && timeDiff <= thirtyMinutes) {
                String msg = String.format("【待办提醒】您的待办事项「%s」将在30分钟内到期，请及时处理！", todo.getTitle());
                SISendUtil.send(msg);
                todo.setReminded(true);
                cacheManager.saveTodo(todo);
            }
        }
    }
}
