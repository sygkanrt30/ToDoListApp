package ru.practise.pet_projects.todolistapp.database;

import lombok.Getter;

@Getter
public enum NameOfSQLRequests {
    USER_INFO_SELECT_BY_LOGIN("USER_INFO_SELECT_BY_LOGIN"),
    USER_INFO_SELECT_BY_USERNAME("USER_INFO_SELECT_BY_USERNAME"),
    TASKS_INFO_SELECT_BY_USERNAME("TASKS_INFO_SELECT_BY_USERNAME"),
    INSERT_USER("INSERT_USER"),
    INSERT_TASK("INSERT_TASK"),
    DELETE_ALL_TASKS("DELETE_ALL_TASKS"),
    DELETE_ALL_COMPLETED_TASKS("DELETE_ALL_COMPLETED_TASKS"),
    EXECUTE_ALL_TASKS("EXECUTE_ALL_TASKS"),
    RENAME_TASK("RENAME_TASK"),
    DELETE_TASK("DELETE_TASK"),
    EXECUTE_TASK("EXECUTE_TASK"),
    CANSEL_EXECUTION_TASK("CANSEL_EXECUTION_TASK"),
    ;

    private final String name;

    NameOfSQLRequests(String name) {
        this.name = name;
    }
}
