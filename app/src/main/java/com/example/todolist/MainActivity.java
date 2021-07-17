package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.example.todolist.adapter.ToDoListAdapter;
import com.example.todolist.model.ToDoListModel;
import com.example.todolist.utils.BaseDonneTaches;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MainActivity extends AppCompatActivity implements DialogCloseListener {

    private RecyclerView rvTache;
    private List<ToDoListModel> listTach ;
    private ToDoListAdapter adapterTach ;
    private BaseDonneTaches db;
    private FloatingActionButton fab ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new BaseDonneTaches(this);
        db.openDatabase();

        fab = findViewById(R.id.fabId);

        listTach = new ArrayList<>();

        rvTache = findViewById(R.id.recycleViewID);
        rvTache.setHasFixedSize(true);
        rvTache.setLayoutManager(new LinearLayoutManager(this));

        adapterTach = new ToDoListAdapter(db, this);
        rvTache.setAdapter(adapterTach);

        listTach = db.getAllTaches();
        Collections.reverse(listTach);
        adapterTach.setTach(listTach);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(adapterTach, listTach));
        itemTouchHelper.attachToRecyclerView(rvTache);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewTache.newInstance().show(getSupportFragmentManager(),AddNewTache.TAG);
            }
        });
    }


    @Override
    public void handleDialogClose(DialogInterface dialog) {
        listTach = db.getAllTaches();
        Collections.reverse(listTach);
        adapterTach.setTach(listTach);
        adapterTach.notifyDataSetChanged();
    }

}