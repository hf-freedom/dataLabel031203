package com.example.todo.entity;

import java.time.LocalDateTime;

/**
 * 待办事项实体类
 */
public class Todo {

    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_IN_PROGRESS = "IN_PROGRESS";
    public static final String STATUS_COMPLETED = "COMPLETED";
    public static final String STATUS_OVERDUE = "OVERDUE";

    private String id;
    private String title;
    private String content;
    private String userId;
    private String createBy;
    private String status;
    private LocalDateTime deadline;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private boolean reminded;

    public Todo() {
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
        this.status = STATUS_PENDING;
        this.reminded = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isReminded() {
        return reminded;
    }

    public void setReminded(boolean reminded) {
        this.reminded = reminded;
    }

    public boolean isOverdue() {
        return LocalDateTime.now().isAfter(this.deadline);
    }

    public boolean isNearDeadline() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime halfHourLater = now.plusMinutes(30);
        return !reminded && !isOverdue() && !deadline.isBefore(now) && !deadline.isAfter(halfHourLater);
    }
}
