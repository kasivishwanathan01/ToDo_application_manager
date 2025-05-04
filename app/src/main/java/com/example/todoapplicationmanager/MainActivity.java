package com.example.todoapplicationmanager;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.RecognitionListener;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements EditTaskDialog.OnTaskEditedListener {

    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    private static final int REQUEST_CODE_AUDIO_PERMISSION = 2000;
    public static final int REQUEST_EDIT_TASK = 1;
    public static final int REQUEST_VIEW_DELETED_TASKS = 2;

    private EditText editTextTask;
    private ImageButton buttonMic;
    private Button buttonSelectDueDate;
    private TextView textViewDueDate;
    private Button buttonAdd;
    private Button buttonViewDeleted;
    private Button buttonUserGuide;
    private RecyclerView recyclerViewTasks;

    private TaskAdapter adapter;
    private List<Task> taskList;
    private List<Task> deletedTaskList;
    private String selectedDueDate;
    private SpeechRecognizer speechRecognizer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextTask = findViewById(R.id.editTextTask);
        buttonMic = findViewById(R.id.buttonMic);
        buttonSelectDueDate = findViewById(R.id.buttonSelectDueDate);
        textViewDueDate = findViewById(R.id.textViewDueDate);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonViewDeleted = findViewById(R.id.buttonViewDeleted);
        buttonUserGuide = findViewById(R.id.buttonUserGuide);
        recyclerViewTasks = findViewById(R.id.recyclerViewTasks);

        taskList = TaskStorage.loadTasks(this);
        deletedTaskList = TaskStorage.loadDeletedTasks(this);
        adapter = new TaskAdapter(this, taskList, deletedTaskList);
        recyclerViewTasks.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTasks.setAdapter(adapter);

        buttonMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAudioPermissionAndStartRecognition();
            }
        });

        buttonSelectDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker();
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTask();
            }
        });

        buttonViewDeleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DeletedTasksActivity.class);
                startActivityForResult(intent, REQUEST_VIEW_DELETED_TASKS);
            }
        });

        buttonUserGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserGuideActivity.class);
                startActivity(intent);
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                int swipeFlags = ItemTouchHelper.LEFT;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                adapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.onItemDismiss(viewHolder.getAdapterPosition());
            }

            @Override
            public void onChildDraw(@NonNull Canvas canvas, @NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    View itemView = viewHolder.itemView;
                    int backgroundCornerOffset = 20;

                    Drawable icon = ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_delete);
                    ColorDrawable background = new ColorDrawable(Color.RED);

                    int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
                    int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
                    int iconBottom = iconTop + icon.getIntrinsicHeight();

                    if (dX < 0) { // Swiping to the left
                        int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
                        int iconRight = itemView.getRight() - iconMargin;
                        icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

                        background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                                itemView.getTop(), itemView.getRight(), itemView.getBottom());
                    } else { // view is unSwiped
                        background.setBounds(0, 0, 0, 0);
                    }

                    background.draw(canvas);
                    icon.draw(canvas);
                }
                super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

            @Override
            public boolean isLongPressDragEnabled() {
                return true;
            }

            @Override
            public boolean isItemViewSwipeEnabled() {
                return true;
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerViewTasks);

        recyclerViewTasks.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerViewTasks, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                // Single tap
            }

            @Override
            public void onLongClick(View view, int position) {
                // Long tap
            }

            @Override
            public void onDoubleTap(View view, int position) {
                // Double tap
                Task task = taskList.get(position);
                EditTaskDialog dialog = new EditTaskDialog(MainActivity.this, task, MainActivity.this);
                dialog.show();
            }
        }));

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {}

            @Override
            public void onBeginningOfSpeech() {}

            @Override
            public void onRmsChanged(float rmsdB) {}

            @Override
            public void onBufferReceived(byte[] buffer) {}

            @Override
            public void onEndOfSpeech() {}

            @Override
            public void onError(int error) {
                if (error == SpeechRecognizer.ERROR_RECOGNIZER_BUSY) {
                    Toast.makeText(MainActivity.this, "Recognizer busy, please wait...", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Recognition error: " + error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null && !matches.isEmpty()) {
                    handleVoiceInput(matches.get(0));
                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {}

            @Override
            public void onEvent(int eventType, Bundle params) {}
        });

        checkAudioPermission();
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        Uri data = intent.getData();
        if (data != null && "example".equals(data.getScheme()) && "createTask".equals(data.getHost())) {
            String taskDescription = data.getQueryParameter("description");
            if (taskDescription != null) {
                editTextTask.setText(taskDescription);
                addTask();
            }
        }
    }

    private void checkAudioPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_CODE_AUDIO_PERMISSION);
        }
    }

    private void checkAudioPermissionAndStartRecognition() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            startVoiceRecognition();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_CODE_AUDIO_PERMISSION);
        }
    }

    private void startVoiceRecognition() {
        if (speechRecognizer != null) {
            speechRecognizer.stopListening();
        }
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now...");
        speechRecognizer.startListening(intent);
    }

    private void handleVoiceInput(String input) {
        String[] parts = input.split("due date");
        String taskDescription = parts[0].trim();
        editTextTask.setText(taskDescription);

        if (parts.length > 1) {
            String dueDateText = parts[1].trim();
            selectedDueDate = parseDueDate(dueDateText);
            textViewDueDate.setText(selectedDueDate);
        }
    }

    private String parseDueDate(String dueDateText) {
        return dueDateText;
    }

    private void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        final Calendar date = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, (view, year, monthOfYear, dayOfMonth) -> {
            date.set(year, monthOfYear, dayOfMonth);
            new TimePickerDialog(MainActivity.this, (view1, hourOfDay, minute) -> {
                date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                date.set(Calendar.MINUTE, minute);
                selectedDueDate = date.getTime().toString();
                textViewDueDate.setText(selectedDueDate);
            }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE));

        // Set the minimum date to today
        datePickerDialog.getDatePicker().setMinDate(currentDate.getTimeInMillis());
        datePickerDialog.show();
    }

    private void addTask() {
        String taskDescription = editTextTask.getText().toString();
        if (taskDescription.isEmpty()) {
            Toast.makeText(this, "Please enter a task description", Toast.LENGTH_SHORT).show();
            return;
        }
        Task newTask = new Task(taskDescription, selectedDueDate, Calendar.getInstance().getTime().toString(), taskList.size());
        taskList.add(newTask);
        adapter.notifyDataSetChanged();
        TaskStorage.saveTasks(this, taskList); // Save the tasks
        editTextTask.setText("");
        textViewDueDate.setText("No due date selected");
        selectedDueDate = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_EDIT_TASK && resultCode == RESULT_OK && data != null) {
            Task updatedTask = (Task) data.getSerializableExtra("updatedTask");
            int position = data.getIntExtra("position", -1);
            if (updatedTask != null && position != -1) {
                taskList.set(position, updatedTask);
                adapter.notifyItemChanged(position);
                TaskStorage.saveTasks(this, taskList); // Save the updated tasks
                Toast.makeText(this, "Task updated", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_VIEW_DELETED_TASKS && resultCode == RESULT_OK && data != null) {
            Task restoredTask = (Task) data.getSerializableExtra("restoredTask");
            if (restoredTask != null) {
                taskList.add(restoredTask);
                adapter.notifyDataSetChanged();
                TaskStorage.saveTasks(this, taskList);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_AUDIO_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startVoiceRecognition();
            } else {
                Toast.makeText(this, "Permission denied to use microphone", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onTaskEdited(Task task) {
        adapter.notifyDataSetChanged();
        TaskStorage.saveTasks(this, taskList);
    }
}