package com.example.todoapplicationmanager;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import java.util.Calendar;

public class EditTaskDialog extends Dialog {

    private EditText editTextTaskDescription;
    private EditText editTextTaskDueDate;
    private Button buttonSelectDueDate;
    private Button buttonSave;
    private Task task;
    private OnTaskEditedListener listener;

    public EditTaskDialog(@NonNull Context context, Task task, OnTaskEditedListener listener) {
        super(context);
        this.task = task;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_edit_task);

        editTextTaskDescription = findViewById(R.id.editTextTaskDescription);
        editTextTaskDueDate = findViewById(R.id.editTextTaskDueDate);
        buttonSelectDueDate = findViewById(R.id.buttonSelectDueDate);
        buttonSave = findViewById(R.id.buttonSave);

        editTextTaskDescription.setText(task.getTitle());
        editTextTaskDueDate.setText(task.getDueDate());

        buttonSelectDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task.setTitle(editTextTaskDescription.getText().toString());
                task.setDueDate(editTextTaskDueDate.getText().toString());
                listener.onTaskEdited(task);
                dismiss();
            }
        });

        // Set dialog size to be larger but not fullscreen
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(lp);
    }

    private void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        final Calendar date = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year, monthOfYear, dayOfMonth) -> {
            date.set(year, monthOfYear, dayOfMonth);
            new TimePickerDialog(getContext(), (view1, hourOfDay, minute) -> {
                date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                date.set(Calendar.MINUTE, minute);
                editTextTaskDueDate.setText(String.format("%02d/%02d/%04d %02d:%02d", dayOfMonth, monthOfYear + 1, year, hourOfDay, minute));
            }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE));

        // Set the minimum date to today
        datePickerDialog.getDatePicker().setMinDate(currentDate.getTimeInMillis());
        datePickerDialog.show();
    }

    public interface OnTaskEditedListener {
        void onTaskEdited(Task task);
    }
}