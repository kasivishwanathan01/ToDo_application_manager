package com.example.todoapplicationmanager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private EditText editTextTask;
    private Button buttonAdd;
    private ListView listViewTasks;
    private ArrayList<String> tasks;
    private TaskAdapter adapter;

    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREFS_NAME = "todo_prefs";
    private static final String TASKS_KEY = "tasks";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Views
        editTextTask = findViewById(R.id.editTextTask);
        buttonAdd = findViewById(R.id.buttonAdd);
        listViewTasks = findViewById(R.id.listViewTasks);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE);

        // Load saved tasks
        tasks = loadTasks();
        if (tasks == null) {
            tasks = new ArrayList<>();
        }

        // Set up the ListView with the custom adapter
        adapter = new TaskAdapter(this, tasks);
        listViewTasks.setAdapter(adapter);

        // Add Task Button Click
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String task = editTextTask.getText().toString().trim();
                if (!task.isEmpty()) {
                    // Get the current date and format it
                    String currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());
                    String taskWithDate = task + "       " + currentDate;

                    tasks.add(taskWithDate);
                    adapter.notifyDataSetChanged();
                    saveTasks();
                    editTextTask.setText("");
                } else {
                    Toast.makeText(MainActivity.this, "Please enter a task", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Remove Task on List Item Click
        listViewTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tasks.remove(position);
                adapter.notifyDataSetChanged();
                saveTasks();
                Toast.makeText(MainActivity.this, "Task removed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Save tasks to SharedPreferences
    private void saveTasks() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> taskSet = new HashSet<>(tasks); // Save tasks as a set of strings
        editor.putStringSet(TASKS_KEY, taskSet);
        editor.apply();
    }

    // Load tasks from SharedPreferences
    private ArrayList<String> loadTasks() {
        Set<String> taskSet = sharedPreferences.getStringSet(TASKS_KEY, null);
        if (taskSet != null) {
            return new ArrayList<>(taskSet);
        }
        return null;
    }
}
