package com.example.todolist.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.todolist.model.ToDoListModel;

import java.util.ArrayList;
import java.util.List;

public class BaseDonneTaches extends SQLiteOpenHelper {

    private static final int version = 1;
    private static final String name = "toDoListBaseDonne" ;

    public BaseDonneTaches(@Nullable Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE todolist (id INTEGER Primary key AUTOINCREMENT, statut INTEGER, tache TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS todolist");
        onCreate(sqLiteDatabase);
    }

    public void openDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
    }

    // ajoute une tache dans la base de donnée
    public void ajouterTache(ToDoListModel model){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("tache",model.getTache());
        cv.put("statut" , 0);
        db.insert("todolist", null , cv);
        db.close();
    }

    // donne tous les taches qui sont dans la base de donnée
    public List<ToDoListModel> getAllTaches(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM todolist" , null);
        List<ToDoListModel> ls = new ArrayList<>();
        cur.moveToFirst();
        while(cur.isAfterLast() == false){
            ToDoListModel model = new ToDoListModel();
            model.setId(cur.getInt(cur.getColumnIndex("id")));
            model.setStatut(cur.getInt(cur.getColumnIndex("statut")));
            model.setTache(cur.getString(cur.getColumnIndex("tache")));
            ls.add(model);
            cur.moveToNext();
        }
        db.close();
        return ls;
    }

    // modifier status dans la base de donnée
    public void modifieStatus(int id , int statut){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("statut", statut);
        db.update("todolist",cv,"id=?" , new String[]{String.valueOf(id)});
        db.close();
    }
    

    public void modifieTaches(int id , String tache){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("tache", tache);
        db.update("todolist",cv,"id=?" , new String[]{String.valueOf(id)});
        db.close();
    }

    public void supprimeTaches(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("todolist","id=?", new String[]{String.valueOf(id)});
        db.close();
    }
}
