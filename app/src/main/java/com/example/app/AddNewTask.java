package com.example.app;


import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.app.model.Todomodel;
import com.example.app.utils.DatabaseHandler;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

public class AddNewTask extends BottomSheetDialogFragment {

    public static final String TAG="ActionBottomDialog";
    private EditText newtasktext;
    private EditText newtaskdesc;
    private Button newTaskSaveButton;
    private DatabaseHandler db;

    public static AddNewTask newInstance(){
        return new AddNewTask();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL,R.style.DialogStyle);


    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.new_task,container,false);
        Objects.requireNonNull(getDialog()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;

    }
    @Override
    public void onViewCreated(@NonNull View view,@Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        newtasktext= getView().findViewById(R.id.name);
        newtaskdesc=getView().findViewById(R.id.Status);
        newTaskSaveButton= getView().findViewById(R.id.save);



        boolean isUpdate=false;
        final Bundle bundle= getArguments();
        if(bundle != null){
            isUpdate=true;
            String task=bundle.getString("task");
            String desc=bundle.getString("descr");
            newtasktext.setText(task);
            newtaskdesc.setText(desc);
            assert task !=null;
            if(task.length()>0) {
                newTaskSaveButton.setTextColor(ContextCompat.getColor(requireContext(), com.google.android.material.R.color.design_default_color_primary_dark));
            }

        }db=new DatabaseHandler(getActivity());
        db.openDatabase();





        newtasktext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if(charSequence.toString().equals("")){
                        newTaskSaveButton.setEnabled(false);;
                        newTaskSaveButton.setTextColor(Color.GRAY);
                    }
                    else{
                        newTaskSaveButton.setEnabled(true);
                        newTaskSaveButton.setTextColor(ContextCompat.getColor(requireContext(),R.color.colorPrimaryDark));
                    }
                }


            @Override
            public void afterTextChanged(Editable editable) {

            }
        });





        boolean finalIsUpdate = isUpdate;
        newTaskSaveButton.setOnClickListener(view1 -> {
            String text=newtasktext.getText().toString();
            String text1=newtaskdesc.getText().toString();
            if(finalIsUpdate){
                db.updatetask(bundle.getInt("id"),text,text1);

            }
            else{
                Todomodel task=new Todomodel();
                Todomodel descr=new Todomodel();
                task.setTask(text);
                task.setStatus(0);
                descr.setDescr(text1);
                db.insertTask(task,descr);

            }
            dismiss();
        });

    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog){
        Activity activity=getActivity();
        if(activity instanceof DialogCloseListener){
            ((DialogCloseListener)activity).handleDialogClose(dialog);
        }

    }


}
