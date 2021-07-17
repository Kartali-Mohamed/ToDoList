package com.example.todolist.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.AddNewTache;
import com.example.todolist.MainActivity;
import com.example.todolist.R;
import com.example.todolist.model.ToDoListModel;
import com.example.todolist.utils.BaseDonneTaches;

import java.util.List;

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ViewHolder> {


    private List<ToDoListModel> listModels ;
    private MainActivity activity ;
    private BaseDonneTaches db ;

    public ToDoListAdapter(BaseDonneTaches db , MainActivity activity)
    {
        this.db = db;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout , parent , false);
        return new ViewHolder(view);
    }

    public boolean toBoolean(int b)
    {
        return b!=0;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position)
    {
        db.openDatabase();
        final ToDoListModel model = listModels.get(position);
        holder.checkTache.setText(model.getTache());
        holder.checkTache.setChecked(toBoolean(model.getStatut()));
        // verifier si la tache est barre ou non par rapport le statut
        {
            if(model.getStatut() == 1){
                holder.checkTache.setPaintFlags(holder.checkTache.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.checkTache.setTextColor(Color.GRAY);
            } else {
                holder.checkTache.setPaintFlags(holder.checkTache.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                holder.checkTache.setTextColor(Color.BLACK);
            }
        }
        holder.checkTache.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    db.modifieStatus(model.getId(),1);
                    holder.checkTache.setPaintFlags(holder.checkTache.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.checkTache.setTextColor(Color.GRAY);
                } else {
                    db.modifieStatus(model.getId(),0);
                    holder.checkTache.setPaintFlags(holder.checkTache.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    holder.checkTache.setTextColor(Color.BLACK);
                }
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return listModels.size();
    }

    public Context getContext()
    {
        return activity;
    }

    public void setTach(List<ToDoListModel> listTach)
    {
        this.listModels = listTach;
        notifyDataSetChanged();
    }

    public void deleteItem(int position)
    {
        ToDoListModel item = listModels.get(position);
        db.supprimeTaches(item.getId());
        listModels.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position)
    {
        ToDoListModel item = listModels.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("tache", item.getTache());
        AddNewTache fragment = new AddNewTache();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewTache.TAG);
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private CheckBox checkTache ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkTache = itemView.findViewById(R.id.checkID);
        }
    }
}
