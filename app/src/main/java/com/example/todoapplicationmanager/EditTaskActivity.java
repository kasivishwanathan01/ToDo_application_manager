package com.example.todoapplicationmanager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class EditTaskActivity extends AppCompatActivity {

    private EditText editTextTaskTitle;
    private Button buttonSelectDueDate;
    private TextView textViewDueDate;
    private Button buttonSave;
    private Task task;
    private int position;
    private String selectedDueDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        editTextTaskTitle = findViewById(R.id.editTextTaskTitle);
        buttonSelectDueDate = findViewById(R.id.buttonSelectDueDate);
        textViewDueDate = findViewById(R.id.textViewDueDate);
        buttonSave = findViewById(R.id.buttonSave);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("task") && intent.hasExtra("position")) {
            task = (Task) intent.getSerializableExtra("task");
            position = intent.getIntExtra("position", -1);

            if (task != null) {
                editTextTaskTitle.setText(task.getTitle());
                textViewDueDate.setText(task.getDueDate());
                selectedDueDate = task.getDueDate();
            }
        }

        buttonSelectDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTask();
            }
        });
    }

    private void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        final Calendar date = Calendar.getInstance();
        new DatePickerDialog(EditTaskActivity.this, (view, year, monthOfYear, dayOfMonth) -> {
            date.set(year, monthOfYear, dayOfMonth);
            new TimePickerDialog(EditTaskActivity.this, (view1, hourOfDay, minute) -> {
                date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                date.set(Calendar.MINUTE, minute);
                selectedDueDate = date.getTime().toString();
                textViewDueDate.setText(selectedDueDate);
            }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }

    private void saveTask() {
        String taskTitle = editTextTaskTitle.getText().toString().trim();
        if (taskTitle.isEmpty()) {
            Toast.makeText(this, "Task title cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        task.setTitle(taskTitle);
        task.setDueDate(selectedDueDate);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("updatedTask", task);
        resultIntent.putExtra("position", position);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}