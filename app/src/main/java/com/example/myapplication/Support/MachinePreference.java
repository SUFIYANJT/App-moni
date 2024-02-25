package com.example.myapplication.Support;
import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
        sharedPreferences.edit().putString(key, machinesJson).apply();
    }

    public ArrayList<Machine> getMachines(String key) {
        String machinesJson = sharedPreferences.getString(key, null);
        if (machinesJson != null) {
            Type type = new TypeToken<ArrayList<Machine>>() {}.getType();
            return gson.fromJson(machinesJson, type);
        }
        return new ArrayList<>(); // Return empty ArrayList if no data found
    }
}

