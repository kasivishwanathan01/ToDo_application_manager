// Generated by view binder compiler. Do not edit!
package com.example.todoapplicationmanager.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.todoapplicationmanager.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ListItemTaskBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final TextView taskDueDate;

  @NonNull
  public final TextView taskTitle;

  private ListItemTaskBinding(@NonNull LinearLayout rootView, @NonNull TextView taskDueDate,
      @NonNull TextView taskTitle) {
    this.rootView = rootView;
    this.taskDueDate = taskDueDate;
    this.taskTitle = taskTitle;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ListItemTaskBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ListItemTaskBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.list_item_task, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ListItemTaskBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.taskDueDate;
      TextView taskDueDate = ViewBindings.findChildViewById(rootView, id);
      if (taskDueDate == null) {
        break missingId;
      }

      id = R.id.taskTitle;
      TextView taskTitle = ViewBindings.findChildViewById(rootView, id);
      if (taskTitle == null) {
        break missingId;
      }

      return new ListItemTaskBinding((LinearLayout) rootView, taskDueDate, taskTitle);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
