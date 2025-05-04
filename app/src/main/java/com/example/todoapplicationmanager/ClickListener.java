package com.example.todoapplicationmanager;

import android.view.View;

public interface ClickListener {
    void onClick(View view, int position);
    void onLongClick(View view, int position);
    void onDoubleTap(View view, int position);
}