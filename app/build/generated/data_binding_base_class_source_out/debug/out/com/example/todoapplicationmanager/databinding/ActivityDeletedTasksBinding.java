// Generated by view binder compiler. Do not edit!
package com.example.todoapplicationmanager.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.todoapplicationmanager.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityDeletedTasksBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final RecyclerView recyclerViewDeletedTasks;

  @NonNull
  public final TextView textViewDeletedTasks;

  @NonNull
  public final TextView textViewNoDeletedTasks;

  private ActivityDeletedTasksBinding(@NonNull LinearLayout rootView,
      @NonNull RecyclerView recyclerViewDeletedTasks, @NonNull TextView textViewDeletedTasks,
      @NonNull TextView textViewNoDeletedTasks) {
    this.rootView = rootView;
    this.recyclerViewDeletedTasks = recyclerViewDeletedTasks;
    this.textViewDeletedTasks = textViewDeletedTasks;
    this.textViewNoDeletedTasks = textViewNoDeletedTasks;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityDeletedTasksBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityDeletedTasksBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_deleted_tasks, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityDeletedTasksBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.recyclerViewDeletedTasks;
      RecyclerView recyclerViewDeletedTasks = ViewBindings.findChildViewById(rootView, id);
      if (recyclerViewDeletedTasks == null) {
        break missingId;
      }

      id = R.id.textViewDeletedTasks;
      TextView textViewDeletedTasks = ViewBindings.findChildViewById(rootView, id);
      if (textViewDeletedTasks == null) {
        break missingId;
      }

      id = R.id.textViewNoDeletedTasks;
      TextView textViewNoDeletedTasks = ViewBindings.findChildViewById(rootView, id);
      if (textViewNoDeletedTasks == null) {
        break missingId;
      }

      return new ActivityDeletedTasksBinding((LinearLayout) rootView, recyclerViewDeletedTasks,
          textViewDeletedTasks, textViewNoDeletedTasks);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
