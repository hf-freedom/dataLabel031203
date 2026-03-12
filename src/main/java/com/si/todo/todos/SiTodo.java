package com.si.todo.todos;

import java.io.Serializable;
import java.util.Date;

public class SiTodo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String userId;
    private String title;
    private String content;
    private SiTodoStatus status;
    private Date deadline;
    private Date createTime;
    private Date updateTime;
    private boolean isBroadcast;
    private boolean reminded;

    public SiTodo() {
        this.status = SiTodoStatus.IN_PROGRESS;
        this.createTime = new Date();
        this.updateTime = new Date();
        this.isBroadcast = false;
        this.reminded = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public SiTodoStatus getStatus() {
        return status;
    }

    public void setStatus(SiTodoStatus status) {
        this.status = status;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isBroadcast() {
        return isBroadcast;
    }

    public void setBroadcast(boolean broadcast) {
        isBroadcast = broadcast;
    }

    public boolean isReminded() {
        return reminded;
    }

    public void setReminded(boolean reminded) {
        this.reminded = reminded;
    }
}
