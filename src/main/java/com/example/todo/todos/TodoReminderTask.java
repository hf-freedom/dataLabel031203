package com.example.todo.todos;

import com.example.todo.entity.Todo;
import com.example.todo.entity.User;
import com.example.todo.util.SISendUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 待办事项提醒定时任务
 */
@Component
public class TodoReminderTask {

    @Autowired
    private TodoService todoService;

    @Autowired
    private UserService userService;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * 每分钟检查一次即将到期的待办事项
     */
    @Scheduled(fixedRate = 60000)
    public void checkNearDeadlineTodos() {
        List<Todo> nearDeadlineTodos = todoService.getNearDeadlineTodos();
        for (Todo todo : nearDeadlineTodos) {
            User user = userService.getUserById(todo.getUserId());
            if (user != null) {
                String msg = String.format("【待办提醒】用户 %s，您的待办事项\"%s\"将在半小时内到期(截止时间：%s)，请尽快处理！",
                        user.getNickname(),
                        todo.getTitle(),
                        todo.getDeadline().format(FORMATTER));
                SISendUtil.send(msg);
                todoService.markReminded(todo.getId());
            }
        }
    }

    /**
     * 每5分钟检查一次逾期待办事项并更新状态
     */
    @Scheduled(fixedRate = 300000)
    public void checkOverdueTodos() {
        todoService.checkAndUpdateOverdue();
    }
}
