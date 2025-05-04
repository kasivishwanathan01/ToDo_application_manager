package com.example.todoapplicationmanager;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Collections;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private final Context context;
    private final List<Task> taskList;
    private List<Task> deletedTaskList;
    private final Handler handler = new Handler(Looper.getMainLooper());

    // Constructor for both active and deleted tasks
    public TaskAdapter(Context context, List<Task> taskList, List<Task> deletedTaskList) {
        this.context = context;
        this.taskList = taskList;
        this.deletedTaskList = deletedTaskList;
    }

    // Constructor for only deleted tasks
    public TaskAdapter(Context context, List<Task> taskList) {
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
    public void onBindViewHolder(@NonNull final TaskViewHolder holder, int position) {
        final Task task = taskList.get(holder.getAdapterPosition());
        holder.taskDescription.setText(task.getTitle());
        holder.taskDueDate.setText(task.getDueDate());
        holder.taskCreatedDate.setText(task.getCreatedDate());

        // Set the avatar text to the first letter of the task description
        if (task.getTitle() != null && !task.getTitle().isEmpty()) {
            holder.taskAvatar.setText(String.valueOf(task.getTitle().charAt(0)).toUpperCase());
        }

        // Change background color based on task status
        if (task.isOverdue()) {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFCDD2")); // Light red color
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }

        // Handle checkbox state changes
        holder.checkBoxTask.setOnCheckedChangeListener(null); // Reset the listener to avoid incorrect state changes
        holder.checkBoxTask.setChecked(task.isCompleted());
        holder.checkBoxTask.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                task.setCompleted(isChecked);
                TaskStorage.saveTasks(context, taskList); // Save the updated tasks

                // Use Handler to post notifyDataSetChanged after RecyclerView layout computation
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        notifyItemChanged(holder.getAdapterPosition()); // Update only the changed item
                    }
                });
            }
        });

        // Add a long click listener to permanently delete the task
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                TaskStorage.permanentlyDeleteTask(context, task);
                taskList.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(taskList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(taskList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    public void onItemDismiss(int position) {
        Task deletedTask = taskList.remove(position);
        if (deletedTaskList != null) {
            deletedTaskList.add(deletedTask);
            TaskStorage.saveDeletedTasks(context, deletedTaskList);
        }
        notifyItemRemoved(position);
        TaskStorage.saveTasks(context, taskList);
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