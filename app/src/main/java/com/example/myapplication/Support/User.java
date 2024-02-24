package com.example.myapplication.Support;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String password;
    private String usermode;
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
}
