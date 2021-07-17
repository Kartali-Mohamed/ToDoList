package com.example.todolist.model;

public class ToDoListModel {

    private int id , statut ;
    private String tache ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatut() {
        return statut;
    }

    public void setStatut(int status) {
        this.statut = status;
    }

    public String getTache() {
        return tache;
    }

    public void setTache(String tache) {
        this.tache = tache;
    }
}
