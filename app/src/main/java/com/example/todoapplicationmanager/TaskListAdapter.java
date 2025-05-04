package com.example.todoapplicationmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskViewHolder> {

    private final Context context;
    private final List<Task> taskList;

    public TaskListAdapter(Context context, List<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.taskDescription.setText(task.getTitle());
        holder.taskDueDate.setText(task.getDueDate());
        holder.taskCreatedDate.setText(task.getCreatedDate());
        holder.checkBoxTask.setChecked(task.isCompleted());

        // Set the avatar text to the first letter of the task description
        if (task.getTitle() != null && !task.getTitle().isEmpty()) {
            holder.taskAvatar.setText(String.valueOf(task.getTitle().charAt(0)).toUpperCase());
        }
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {

        TextView taskAvatar;
        TextView taskDescription;
        TextView taskDueDate;
        TextView taskCreatedDate;
        CheckBox checkBoxTask;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskAvatar = itemView.findViewById(R.id.taskAvatar);
            taskDescription = itemView.findViewById(R.id.taskDescription);
            taskDueDate = itemView.findViewById(R.id.taskDueDate);
            taskCreatedDate = itemView.findViewById(R.id.taskCreatedDate);
            checkBoxTask = itemView.findViewById(R.id.checkBoxTask);
        }
    }
}