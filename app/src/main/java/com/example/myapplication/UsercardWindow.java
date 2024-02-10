package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class UsercardWindow extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usercardwindow);

        // Assuming you have a MaterialButton with the id menu_button1
        MaterialButton backButton = findViewById(R.id.back_buttonuserwindow);

        // Set click listener for the back button
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(UsercardWindow.this, newmainuser.class);

            startActivity(intent);
            finish();

        });
    }
}
