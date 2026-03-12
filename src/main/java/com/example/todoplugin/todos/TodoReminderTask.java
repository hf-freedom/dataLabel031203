package com.example.todoplugin.todos;

import com.example.todoplugin.map.SITodoCache;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class TodoReminderTask {

    @Scheduled(fixedRate = 60000)
    public void checkTodoReminder() {
        List<Todo> todos = SITodoCache.getAllTodos();
        Date now = new Date();
        long thirtyMinutes = 30 * 60 * 1000;

        for (Todo todo : todos) {
            if (!todo.isNotified() && "IN_PROGRESS".equals(todo.getStatus()) && todo.getDeadline() != null) {
                long timeDiff = todo.getDeadline().getTime() - now.getTime();
                if (timeDiff > 0 && timeDiff <= thirtyMinutes) {
                    String msg = "提醒：您的代办事项 '" + todo.getContent() + "' 将在30分钟内到期，请及时处理！";
                    SISendUtil.send(msg);
                    todo.setNotified(true);
                    SITodoCache.updateTodo(todo);
                }
            }
        }
    }
}
