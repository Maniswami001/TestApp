package com.example.app.utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.fragment.app.FragmentActivity;

import com.example.app.model.Todomodel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int VERSION=1;
    private static final String NAME="Tododb";
    private static final String TODO_TABLE="todo";
    private static final String ID="id";
    private static final String TASK="task";
    private static final String STATUS="status";
    private static final String DESC="description";
    private static final String CREATE_TODO_TABLE="CREATE TABLE " + TODO_TABLE + "(" +ID+  " INTEGER PRIMARY KEY AUTOINCREMENT, " + TASK +  " TEXT, "   +STATUS+ " INTEGER,"  +DESC +   " TEXT ) ";

    private SQLiteDatabase db;

    public DatabaseHandler(FragmentActivity context){
        super(context,NAME,null,VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " +TODO_TABLE);
        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +TODO_TABLE);
        onCreate(db);

    }
    public void openDatabase(){
        db=this.getWritableDatabase();
    }

    public void insertTask(Todomodel task,Todomodel desc){
        ContentValues cv=new ContentValues();
        cv.put(TASK,task.getTask());
        cv.put(STATUS,0);
        cv.put(DESC,desc.getDescr());
        db.insert(TODO_TABLE,null,cv);
    }

    @SuppressLint("Range")
    public List<Todomodel> getalltasks() {
        List<Todomodel> tasklist = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try {
            cur = db.query(TODO_TABLE, null, null, null, null, null, null, null);
            if (cur!=null) {
                if (cur.moveToFirst()) {

                    do {
                        Todomodel task = new Todomodel();
                        task.setId(cur.getInt(cur.getColumnIndex(ID)));
                        task.setTask(cur.getString(cur.getColumnIndex(TASK)));
                        task.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));
                        task.setDescr(cur.getString(cur.getColumnIndex(DESC)));
                        tasklist.add(task);
                    } while (cur.moveToNext());

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            cur.close();

        }
        return tasklist;
    }

    public void updatestatus(int id,int status){

        ContentValues cv=new ContentValues();
        cv.put(STATUS,status);
        db.update(TODO_TABLE,cv, ID + "=?",new String[] {String.valueOf(id)});

    }
    public void updatetask(int id,String task,String descr){
        ContentValues cv=new ContentValues();
        cv.put(TASK,task);
        cv.put(DESC,descr);
        db.update(TODO_TABLE, cv, ID + "=?",new String[] {String.valueOf(id)});


    }
    public void deletetask(int id){
        db.delete(TODO_TABLE, ID + "=?",new String[] {String.valueOf(id)});

    }


}
