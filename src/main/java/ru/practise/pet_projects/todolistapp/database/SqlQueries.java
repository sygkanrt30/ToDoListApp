package ru.practise.pet_projects.todolistapp.database;

import ru.practise.pet_projects.todolistapp.utils.PropertyReader;

public class SqlQueries {

    private static final PropertyReader propertyReader = new PropertyReader("queries.sql");

    public static String getUserCreationQuery() {
        return propertyReader.get("INSERT_USER");
    }

    public static String getUserTasksQuery() {
        return propertyReader.get("TASKS_INFO_SELECT_BY_USERNAME");
    }

    public static String  getUserByLoginQuery(){
        return propertyReader.get("USER_INFO_SELECT_BY_LOGIN");
    }

    public static String  getUserByUsernameQuery(){
        return propertyReader.get("USER_INFO_SELECT_BY_USERNAME");
    }

    public static String getTaskCreationQuery() {
        return propertyReader.get("INSERT_TASK");
    }

    public static String getRemoveAllTasksQuery(){
        return propertyReader.get("DELETE_ALL_TASKS");
    }

    public static String getRemoveAllCompletedTasksQuery(){
        return propertyReader.get("DELETE_ALL_COMPLETED_TASKS");
    }

    public static String getExecuteAllTasksQuery(){
        return propertyReader.get("EXECUTE_ALL_TASKS");
    }

    public static String getRenameTaskQuery(){
        return propertyReader.get("RENAME_TASK");
    }

    public static String getExecuteTaskQuery(){
        return propertyReader.get("EXECUTE_TASK");
    }

    public static String getRemoveTaskQuery(){
        return propertyReader.get("DELETE_TASK");
    }

    public static String getCanselExecutionTaskQuery(){
        return propertyReader.get("CANSEL_EXECUTION_TASK");
    }
}
