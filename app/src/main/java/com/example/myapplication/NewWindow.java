package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.model.ItemModel;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class NewWindow extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_window);

        // Assuming you have a MaterialButton with the id menu_button1
        MaterialButton backButton = findViewById(R.id.back_buttonins);

        // Set click listener for the back button
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(NewWindow.this, MainActivity.class);

            startActivity(intent);
            finish();

        });
    }
}
