package com.example.todoapplicationmanager;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Task implements Serializable {
    private String title;
    private String dueDate;
    private String createdDate;
    private boolean isCompleted;
    private int priority;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    // Constructor
    public Task(String title, String dueDate, String createdDate, int priority) {
        this.title = title;
        this.dueDate = dueDate;
        this.createdDate = createdDate;
        this.isCompleted = false;
        this.priority = priority;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isOverdue() {
        if (isCompleted || dueDate == null) {
            return false;
        }
        try {
            Date dueDateObj = dateFormat.parse(dueDate);
            return dueDateObj != null && new Date().after(dueDateObj);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
}