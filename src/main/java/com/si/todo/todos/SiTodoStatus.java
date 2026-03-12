package com.si.todo.todos;

public enum SiTodoStatus {
    IN_PROGRESS("进行中"),
    COMPLETED("完成"),
    OVERDUE("逾期");

    private String desc;

    SiTodoStatus(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
