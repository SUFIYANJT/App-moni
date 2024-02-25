package com.example.myapplication.Support;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Machine implements Serializable {
    private int id;
    private String name;
    private int value;
    private int currentvalue;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getCurrentvalue() {
        return currentvalue;
    }

    public void setCurrentvalue(int currentvalue) {
        this.currentvalue = currentvalue;
    }

    @NonNull
    @Override
    public String toString() {
        return this.name;
    }
}
