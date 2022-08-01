package com.example.app;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.adapter.Todoadapter;
import com.example.app.model.Todomodel;
import com.example.app.utils.DatabaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogCloseListener  {

    private RecyclerView tasksRecyclerview;
    private FloatingActionButton add;
    private Todoadapter tasksAdapter;
    private List<Todomodel> tasklist;
    private DatabaseHandler db;
    private static final String TAG="MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        tasklist = new ArrayList<>();
        add=findViewById(R.id.add);
        db=new DatabaseHandler(this);
        db.openDatabase();


        tasksRecyclerview = findViewById(R.id.list);
        tasksRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        tasksAdapter = new Todoadapter(db,this);
        tasksRecyclerview.setAdapter(tasksAdapter);

        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(new RecyclerItemTouchHelper(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerview);

        tasklist=db.getalltasks();
        Collections.reverse(tasklist);
        tasksAdapter.setTasks(tasklist);


        add.setOnClickListener(view -> AddNewTask.newInstance().show(getSupportFragmentManager(),AddNewTask.TAG));
    }
    @Override
    public void handleDialogClose(DialogInterface dialog) {
        tasklist=db.getalltasks();
        Collections.reverse(tasklist);
        tasksAdapter.setTasks(tasklist);
        tasksAdapter.notifyDataSetChanged();
    }


}