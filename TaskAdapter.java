package com.example.todoapplicationmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.ArrayAdapter;

import java.util.List;

public class TaskAdapter extends ArrayAdapter<String> {

    public TaskAdapter(@NonNull Context context, @NonNull List<String> tasks) {
        super(context, 0, tasks);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_task, parent, false);
        }

        // Get the task with date
        String taskWithDate = getItem(position);

        // Split the task and date
        String[] parts = taskWithDate.split("       "); // Adjust the delimiter based on your format
        String task = parts[0];
        String date = parts.length > 1 ? parts[1] : "";

        // Bind data to views
        TextView taskText = convertView.findViewById(R.id.taskText);
        TextView taskDate = convertView.findViewById(R.id.taskDate);

        taskText.setText(task);
        taskDate.setText(date);

        return convertView;
    }
}
