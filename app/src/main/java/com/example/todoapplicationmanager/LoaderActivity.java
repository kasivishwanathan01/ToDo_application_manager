package com.example.todoapplicationmanager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class LoaderActivity extends AppCompatActivity {

    private static final int LOADER_DISPLAY_TIME = 3000; // 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader);

        // Handler to delay the transition from the loader screen
        new Handler().postDelayed(() -> {
            // Start the main activity after the loader screen
            Intent intent = new Intent(LoaderActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, LOADER_DISPLAY_TIME);
    }
}