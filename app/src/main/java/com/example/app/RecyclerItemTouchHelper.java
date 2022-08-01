package com.example.app;


import static androidx.recyclerview.widget.ItemTouchHelper.LEFT;
import static androidx.recyclerview.widget.ItemTouchHelper.RIGHT;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.adapter.Todoadapter;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {

    private Todoadapter adapter;

    public RecyclerItemTouchHelper(Todoadapter adapter){
        super(0, LEFT | RIGHT);
        this.adapter=adapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        final int position=viewHolder.getBindingAdapterPosition();
        if(direction == LEFT){
            AlertDialog.Builder builder=new AlertDialog.Builder(adapter.getcontext());
            builder.setTitle("Delete Task");
            builder.setMessage("Are you sure want to delete the task?");
            builder.setPositiveButton("confirm",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            adapter.Deleteitem(position);
                        }
                    });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    adapter.notifyItemChanged(viewHolder.getBindingAdapterPosition());
                }
            });
            AlertDialog dialog=builder.create();
            dialog.show();
        }
        else{
            adapter.Edititem(position);
        }

    }
    @Override
    public void onChildDraw(Canvas c,RecyclerView recyclerView,RecyclerView.ViewHolder viewHolder,float dX,float dY,int actionState,boolean iscurrentlyActive){
        super.onChildDraw(c,recyclerView,viewHolder,dX,dY,actionState,iscurrentlyActive);

        Drawable icon;
        ColorDrawable background;
        View itemview = viewHolder.itemView;
        int backgroundCornerOffset=20;
        if(dX>0){
            icon= ContextCompat.getDrawable(adapter.getcontext(),R.drawable.ic_baseline_edit_24);
            background=new ColorDrawable(ContextCompat.getColor(adapter.getcontext(),R.color.colorPrimaryDark));
        }
        else{
            icon= ContextCompat.getDrawable(adapter.getcontext(),R.drawable.ic_baseline_delete_24);
            background=new ColorDrawable(Color.RED);

        }
        assert icon !=null;
        int iconmargin=(itemview.getHeight() - icon.getIntrinsicHeight());
        int icontop= itemview.getTop() + (itemview.getHeight() - icon.getIntrinsicHeight());
        int iconbottom=icontop + icon.getIntrinsicHeight();

        if(dX>0){
            int iconleft=itemview.getLeft() + iconmargin;
            int iconright=itemview.getLeft()+ iconmargin+ icon.getIntrinsicWidth();
            icon.setBounds(iconleft,icontop,iconright,iconbottom);

            background.setBounds(itemview.getLeft(),itemview.getTop(),itemview.getLeft()+ ((int)dX) + backgroundCornerOffset,itemview.getBottom());


        }
        else if(dX<0){
            int iconleft=itemview.getRight() - iconmargin - icon.getIntrinsicWidth() ;
            int iconright=itemview.getRight() - iconmargin;
            icon.setBounds(iconleft,icontop,iconright,iconbottom);

            background.setBounds(itemview.getRight() + ((int)dX) -backgroundCornerOffset,itemview.getTop(),itemview.getRight(),itemview.getBottom());

        }
        else{
          icon.setBounds(0,0,0,0);
        }
        background.draw(c);
        icon.draw(c);
    }

}
