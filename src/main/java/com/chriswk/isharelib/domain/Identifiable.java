package com.chriswk.isharelib.domain;

public class Identifiable {
    private int id;

    public int getId() {
        return id;
    }

    public Identifiable() {
    }

    public Identifiable(int id) {
        this.id = id;
    }
    public void setId(int id) {
        this.id = id;
    }
}
