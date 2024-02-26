package com.example.myapplication.Support;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String password;
    private String usermode;
    private int user_id;
    public User(String username,String password,String usermode){
        this.username=username;
        this.password=password;
        this.usermode=usermode;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getUsermode() {
        return usermode;
    }

    @NonNull
    @Override
    public String toString() {
        return this.getUsername();
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
