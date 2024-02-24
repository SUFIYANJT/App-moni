package com.example.myapplication;

import com.google.gson.Gson;

public interface NetworkConnector {
    public int checkAuth(String username,String password);
    public int sendMessage(Gson gson);
    public Gson message();
}
