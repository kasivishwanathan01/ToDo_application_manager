package com.example.todoapplicationmanager;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TaskStorage {

    private static final String PREFS_NAME = "task_prefs";
    private static final String TASK_LIST_KEY = "task_list";
    private static final String DELETED_TASK_LIST_KEY = "deleted_task_list";

    // Method to load tasks
    public static List<Task> loadTasks(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(TASK_LIST_KEY, null);
        if (json != null) {
            Type type = new TypeToken<ArrayList<Task>>() {}.getType();
            return new Gson().fromJson(json, type);
        } else {
            return new ArrayList<>();
        }
    }

    // Method to save tasks
    public static void saveTasks(Context context, List<Task> taskList) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String json = new Gson().toJson(taskList);
        editor.putString(TASK_LIST_KEY, json);
        editor.apply();
    }

    // Method to load deleted tasks
    public static List<Task> loadDeletedTasks(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(DELETED_TASK_LIST_KEY, null);
        if (json != null) {
            Type type = new TypeToken<ArrayList<Task>>() {}.getType();
            return new Gson().fromJson(json, type);
        } else {
            return new ArrayList<>();
        }
    }

    // Method to save deleted tasks
    public static void saveDeletedTasks(Context context, List<Task> deletedTaskList) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String json = new Gson().toJson(deletedTaskList);
        editor.putString(DELETED_TASK_LIST_KEY, json);
        editor.apply();
    }

    // Method to delete a single task
    public static void deleteTask(Context context, Task task) {
        List<Task> taskList = loadTasks(context);
        taskList.remove(task);
        saveTasks(context, taskList);

        List<Task> deletedTaskList = loadDeletedTasks(context);
        deletedTaskList.add(task);
        saveDeletedTasks(context, deletedTaskList);
    }

    // Method to permanently delete a task from the deleted list
    public static void permanentlyDeleteTask(Context context, Task task) {
        List<Task> deletedTaskList = loadDeletedTasks(context);
        if (deletedTaskList != null) {
            deletedTaskList.remove(task);
            saveDeletedTasks(context, deletedTaskList);
        }
    }
}