package com.example.todolist;

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

import com.example.todolist.model.ToDoListModel;
import com.example.todolist.utils.BaseDonneTaches;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

public class AddNewTache extends BottomSheetDialogFragment {
    public static final String TAG = "ActionBottomDialog";
    private EditText edTach;
    private Button buSave;

    private BaseDonneTaches db;

    public static AddNewTache newInstance(){
        return new AddNewTache();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.new_task, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edTach = Objects.requireNonNull(getView()).findViewById(R.id.edTachID);
        buSave = getView().findViewById(R.id.buSaveID);

        boolean isUpdate = false;

        final Bundle bundle = getArguments();
        if(bundle != null){
            isUpdate = true;
            String tach = bundle.getString("tache");
            edTach.setText(tach);
            assert tach != null;
            if(tach.length()>0){
                buSave.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorPrimaryDark));
                buSave.setEnabled(true);
            }

        }

        db = new BaseDonneTaches(getActivity());
        db.openDatabase();

        edTach.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    buSave.setEnabled(false);
                    buSave.setTextColor(Color.GRAY);
                }
                else{
                    buSave.setEnabled(true);
                    buSave.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorPrimaryDark));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        final boolean finalIsUpdate = isUpdate;
        buSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = edTach.getText().toString();
                if(finalIsUpdate){
                    db.modifieTaches(bundle.getInt("id"), text);
                }
                else {
                    ToDoListModel model = new ToDoListModel();
                    model.setTache(text);
                    model.setStatut(0);
                    db.ajouterTache(model);
                }
                dismiss();
            }
        });
    }

    @Override
    public void onDismiss(DialogInterface dialog){
        Activity activity = getActivity();
        if(activity instanceof DialogCloseListener)
            ((DialogCloseListener)activity).handleDialogClose(dialog);
    }
}
