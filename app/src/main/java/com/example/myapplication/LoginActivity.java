package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout textInputUsername;
    private TextInputLayout textInputPassword;
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize TextInputLayout and Button
        textInputUsername = findViewById(R.id.text_input_username);
        textInputPassword = findViewById(R.id.text_input_password);
        buttonLogin = findViewById(R.id.button_login);

        // Set OnClickListener for the login button
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve input values
                String username = textInputUsername.getEditText().getText().toString().trim();
                String password = textInputPassword.getEditText().getText().toString().trim();

                // Validate input
                if (validateInput(username, password)) {
                    // Perform login operation (replace this with your actual login logic)
                    if (username.equals("admin") && password.equals("admin")) {
                        // Successful login
                        // Start MainActivity
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        // Finish the current activity
                        finish();
                    } else {
                        // Invalid credentials
                        // You can display an error message to the user
                        System.out.println("Invalid username or password");
                    }
                }
            }
        });
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve input values
                String username = textInputUsername.getEditText().getText().toString().trim();
                String password = textInputPassword.getEditText().getText().toString().trim();

                // Validate input
                if (validateInput(username, password)) {
                    // Perform login operation (replace this with your actual login logic)
                    if (username.equals("admin") && password.equals("admin")) {
                        // Successful login
                        // Start MainActivity
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        // Finish the current activity
                        finish();
                    } else {
                        // Invalid credentials
                        // You can display an error message to the user
                        System.out.println("Invalid username or password");
                    }
                }
            }
        });


    }

    // Method to validate input fields
    private boolean validateInput(String username, String password) {
        if (username.isEmpty()) {
            textInputUsername.setError("Username cannot be empty");
            return false;
        } else {
            textInputUsername.setError(null);
        }

        if (password.isEmpty()) {
            textInputPassword.setError("Password cannot be empty");
            return false;
        } else {
            textInputPassword.setError(null);
        }

        return true;
    }
}
