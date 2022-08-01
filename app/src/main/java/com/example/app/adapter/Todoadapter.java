package com.example.app.adapter;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.AddNewTask;
import com.example.app.MainActivity;
import com.example.app.R;
import com.example.app.model.Todomodel;
import com.example.app.utils.DatabaseHandler;

import java.util.List;

public class Todoadapter extends RecyclerView.Adapter<Todoadapter.ViewHolder> {

    private List<Todomodel> todolist;
    private MainActivity Activity;
    private DatabaseHandler db;

    public Todoadapter(DatabaseHandler db,MainActivity Activity){
        this.db=db;
        this.Activity=Activity;
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.add_task,parent,false);
        return new ViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        db.openDatabase();
        Todomodel item=todolist.get(position);
        holder.task.setText(item.getTask());
        holder.task.setChecked(toBoolean(item.getStatus()));
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    db.updatestatus(item.getId(),1);
                }
                else{
                    db.updatestatus(item.getId(),0);
                }
            }
        });


    }
    private boolean toBoolean(int n){
        return n!=0;
    }



    @Override
    public int getItemCount() {

       return todolist.size();
    }
    public Context getcontext(){return Activity;}

    public void setTasks(List<Todomodel> todolist){
        this.todolist=todolist;
        notifyDataSetChanged();
    }

    public void Deleteitem(int position){
        Todomodel item=todolist.get(position);
        db.deletetask(item.getId());
        todolist.remove(position);
        notifyItemRemoved(position);
    }

    public void Edititem(int position){
        Todomodel item=todolist.get(position);
        Bundle bundle=new Bundle();
        bundle.putInt("id",item.getId());
        bundle.putString("task",item.getTask());
        AddNewTask fragment=new AddNewTask();
        fragment.setArguments(bundle);
        fragment.show(Activity.getSupportFragmentManager(), AddNewTask.TAG);


    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox task;
        ViewHolder(View view){
            super(view);
            task=view.findViewById(R.id.checkbox);
        }

    }


}
