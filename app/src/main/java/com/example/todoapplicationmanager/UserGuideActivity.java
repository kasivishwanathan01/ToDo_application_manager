package com.example.todoapplicationmanager;

import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class UserGuideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_guide);

        TextView textViewUserGuide = findViewById(R.id.textViewUserGuide);
        textViewUserGuide.setText(getUserGuideContent());
    }

    private String getUserGuideContent() {
        return "Welcome to the ToDo Application Manager! This application helps you manage your tasks efficiently. You can add new tasks, edit existing ones, delete tasks, and even restore deleted tasks. This guide will walk you through the various functionalities of the application.\n\n" +
                "Main Features:\n" +
                "1. Add a Task\n" +
                "2. Edit a Task\n" +
                "3. Delete a Task\n" +
                "4. View Deleted Tasks\n" +
                "5. Restore a Deleted Task\n" +
                "6. Permanently Delete a Task\n" +
                "7. User Guide\n\n" +
                "1. Add a Task:\n" +
                "   - Open the application.\n" +
                "   - Enter the task description in the \"Enter task description\" field.\n" +
                "   - Click on the \"Select Due Date and Time\" button to set a due date and time.\n" +
                "   - Click on the \"Add\" button to add the task to the list.\n\n" +
                "2. Edit a Task:\n" +
                "   - Click on the task you want to edit from the main task list.\n" +
                "   - In the Edit Task screen, update the task description or due date.\n" +
                "   - Click on the \"Save\" button to save the changes.\n\n" +
                "3. Delete a Task:\n" +
                "   - Swipe the task to the right in the main task list.\n" +
                "   - The task will be moved to the deleted tasks list.\n\n" +
                "4. View Deleted Tasks:\n" +
                "   - Click on the \"View Deleted Tasks\" button.\n" +
                "   - You will be taken to the Deleted Tasks screen where you can see all the tasks that have been deleted.\n\n" +
                "5. Restore a Deleted Task:\n" +
                "   - In the Deleted Tasks screen, swipe the task to the right.\n" +
                "   - The task will be moved back to the main task list.\n\n" +
                "6. Permanently Delete a Task:\n" +
                "   - In the Deleted Tasks screen, swipe the task to the left.\n" +
                "   - The task will be permanently deleted from the application.\n\n" +
                "7. User Guide:\n" +
                "   - Click on the \"User Guide\" button in the main screen.\n" +
                "   - You will be taken to the User Guide screen where you can read this guide.\n\n" +
                "Conclusion:\n" +
                "   This ToDo Application Manager is designed to help you manage your tasks efficiently. With features to add, edit, delete, restore, and view tasks, you can stay organized and on top of your to-do list. If you have any questions or need further assistance, please refer to this user guide.\n\n" +
                "Thank you for using the ToDo Application Manager!";
    }
}