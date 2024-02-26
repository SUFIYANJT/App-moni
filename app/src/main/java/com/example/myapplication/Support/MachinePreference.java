package com.example.myapplication.Support;

import static com.example.myapplication.LoginActivity.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MachinePreference {
    private static final String PREF_NAME = "MachinePrefs";
    private SharedPreferences sharedPreferences;
    private Gson gson;

    public MachinePreference(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public void saveMachines(String key, ArrayList<Machine> machines) {
        String machinesJson = gson.toJson(machines);
        Log.d(TAG, "saveMachines: casted to string "+machinesJson);
        sharedPreferences.edit().putString(key, machinesJson).apply();
    }

    public ArrayList<Machine> getMachines(String key) {
        String machinesJson = sharedPreferences.getString(key, null);
        if (machinesJson != null) {
            Type type = new TypeToken<ArrayList<Machine>>() {}.getType();
            ArrayList<Machine> machines=gson.fromJson(machinesJson, type);
            Log.d(TAG, "getMachines: machines "+machines.get(0).getName());
            return machines;
        }
        return new ArrayList<>(); // Return empty ArrayList if no data found
    }
}


