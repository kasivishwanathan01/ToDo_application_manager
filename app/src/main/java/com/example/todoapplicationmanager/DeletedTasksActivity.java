package com.example.todoapplicationmanager;

import android.content.Intent;
import android.os.Bundle;
import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DeletedTasksActivity extends AppCompatActivity {

    private RecyclerView recyclerViewDeletedTasks;
    private TextView textViewNoDeletedTasks;
    private TaskAdapter adapter;
    private List<Task> deletedTaskList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deleted_tasks);

        recyclerViewDeletedTasks = findViewById(R.id.recyclerViewDeletedTasks);
        textViewNoDeletedTasks = findViewById(R.id.textViewNoDeletedTasks);

        deletedTaskList = TaskStorage.loadDeletedTasks(this);

        if (deletedTaskList.isEmpty()) {
            recyclerViewDeletedTasks.setVisibility(View.GONE);
            textViewNoDeletedTasks.setVisibility(View.VISIBLE);
        } else {
            recyclerViewDeletedTasks.setVisibility(View.VISIBLE);
            textViewNoDeletedTasks.setVisibility(View.GONE);

            adapter = new TaskAdapter(this, deletedTaskList);
            recyclerViewDeletedTasks.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewDeletedTasks.setAdapter(adapter);

            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToRestoreCallback(this) {
                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    int position = viewHolder.getAdapterPosition();
                    Task restoredTask = deletedTaskList.remove(position);
                    TaskStorage.saveDeletedTasks(DeletedTasksActivity.this, deletedTaskList);

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("restoredTask", restoredTask);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                    Toast.makeText(DeletedTasksActivity.this, "Task restored", Toast.LENGTH_SHORT).show();
                }
            });
            itemTouchHelper.attachToRecyclerView(recyclerViewDeletedTasks);
        }
    }
}