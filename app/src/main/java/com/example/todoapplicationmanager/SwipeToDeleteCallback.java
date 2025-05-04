package com.example.todoapplicationmanager;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public abstract class SwipeToDeleteCallback extends ItemTouchHelper.Callback {

    private final Paint paint;
    private final ColorDrawable background;
    private final int backgroundColor;
    private final Drawable deleteIcon;
    private final int intrinsicWidth;
    private final int intrinsicHeight;

    protected SwipeToDeleteCallback(Context context) {
        background = new ColorDrawable();
        backgroundColor = Color.parseColor("#f44336"); // Red color
        paint = new Paint();
        deleteIcon = ContextCompat.getDrawable(context, R.drawable.ic_delete); // Ensure you have a delete icon drawable
        intrinsicWidth = deleteIcon.getIntrinsicWidth();
        intrinsicHeight = deleteIcon.getIntrinsicHeight();
    }

    @Override
    public void onChildDraw(@NonNull Canvas canvas, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            View itemView = viewHolder.itemView;
            int itemHeight = itemView.getHeight();

            boolean isCanceled = dX == 0 && !isCurrentlyActive;

            if (isCanceled) {
                clearCanvas(canvas, (float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                return;
            }

            // Draw the red delete background
            background.setColor(backgroundColor);
            background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
            background.draw(canvas);

            // Calculate position of delete icon
            int deleteIconTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
            int deleteIconMargin = (itemHeight - intrinsicHeight) / 2;
            int deleteIconLeft = itemView.getRight() - deleteIconMargin - intrinsicWidth;
            int deleteIconRight = itemView.getRight() - deleteIconMargin;
            int deleteIconBottom = deleteIconTop + intrinsicHeight;

            // Draw the delete icon
            deleteIcon.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom);
            deleteIcon.draw(canvas);
        }
    }

    private void clearCanvas(Canvas canvas, Float left, Float top, Float right, Float bottom) {
        canvas.drawRect(left, top, right, bottom, paint);
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        // This method will be implemented in MainActivity
    }
}