package com.example.todoplugin.todos;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class Todo {
    private Long id;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date deadline;
    private Long userId;
    private String status;
    private boolean notified;

    public Todo() {
    }

    public Todo(Long id, String content, Date deadline, Long userId, String status) {
        this.id = id;
        this.content = content;
        this.deadline = deadline;
        this.userId = userId;
        this.status = status;
        this.notified = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isNotified() {
        return notified;
    }

    public void setNotified(boolean notified) {
        this.notified = notified;
    }
}
