package com.todolist.todo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.todolist.todo.Adapter.ToDoAdapter;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {

    private ToDoAdapter adapter;

    public RecyclerItemTouchHelper(ToDoAdapter adapter){
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;
    }


    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        final int position = viewHolder.getAdapterPosition();
        if (direction == ItemTouchHelper.LEFT){
            AlertDialog.Builder builder = new AlertDialog.Builder(adapter.getContext());
            builder.setTitle("Delete Task")
                    .setMessage("Do you really want to delete this task?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            adapter.deleteItem(position);
                            Snackbar.make(viewHolder.itemView.getRootView(), "Task Deleted", 3000)
                                    .setTextColor(Color.WHITE)
                                    .setBackgroundTint(Color.GRAY)
                                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                                    .show();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                        }
                    })
                    .setCancelable(true);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else {
            adapter.editItem(position);
            Snackbar.make(viewHolder.itemView.getRootView(), "Task Updated", 3000)
                    .setTextColor(Color.WHITE)
                    .setBackgroundTint(Color.GRAY)
                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                    .show();
        }
    }

    @Override
    public void onChildDraw (Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive){
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        Drawable icon;
        ColorDrawable background;

        View view = viewHolder.itemView;
        int backgroundCornerOffset = 20;

        if (dX<0){
            icon = ContextCompat.getDrawable(adapter.getContext(), R.drawable.ic_delete_24);
            background = new ColorDrawable(ContextCompat.getColor(adapter.getContext(), R.color.red));
        }else {
            icon = ContextCompat.getDrawable(adapter.getContext(), R.drawable.ic_edit_24);
            background = new ColorDrawable(ContextCompat.getColor(adapter.getContext(), R.color.green));
        }

        int iconMargin = (view.getHeight() - icon.getIntrinsicHeight()) /2;
        int iconTop = view.getTop() + (view.getHeight() - icon.getIntrinsicHeight())/2;
        int iconBottom = iconTop + icon.getIntrinsicHeight();

        if (dX>0){ //right swipe
            int iconLeft = view.getLeft() + iconMargin;
            int iconRight = view.getLeft() + iconMargin + icon.getIntrinsicWidth();
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            background.setBounds(view.getLeft(), view.getTop(), view.getLeft() + ((int)dX) + backgroundCornerOffset, view.getBottom());
        }
        else if (dX<0){ //left swipe
            int iconLeft = view.getRight() - iconMargin - icon.getIntrinsicWidth();
            int iconRight = view.getRight() - iconMargin ;
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            background.setBounds(view.getRight() + ((int)dX) - backgroundCornerOffset, view.getTop(), view.getRight() , view.getBottom());

        }
        else {
            background.setBounds(0,0,0,0);
        }

        background.draw(c);
        icon.draw(c);
    }

}
